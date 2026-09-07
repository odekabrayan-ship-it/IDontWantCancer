package com.idontwantcancer.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idontwantcancer.app.core.concurrent.CoroutineDispatcherProvider
import com.idontwantcancer.app.domain.engine.IntelligenceCycleCoordinator
import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.HealingRepository
import com.idontwantcancer.app.domain.repository.UserContextRepository
import com.idontwantcancer.app.domain.usecase.GetCurrentBriefingUseCase
import com.idontwantcancer.app.domain.usecase.GetHealingLogUseCase
import com.idontwantcancer.app.domain.usecase.GetPreventionActionsUseCase
import com.idontwantcancer.app.presentation.boundary.*
import com.idontwantcancer.app.presentation.mapper.toContract
import com.idontwantcancer.app.presentation.mapper.toUiState
import com.idontwantcancer.app.presentation.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentBriefingUseCase: GetCurrentBriefingUseCase,
    private val coordinator: IntelligenceCycleCoordinator,
    private val userContextRepository: UserContextRepository,
    private val healingRepository: HealingRepository,
    private val getPreventionActionsUseCase: GetPreventionActionsUseCase,
    private val getHealingLogUseCase: GetHealingLogUseCase,
    private val resultHandoverBridge: IntelligenceCommandExecutionResultHandoverBoundary,
    private val lifecycleBoundary: IntelligenceCommandLifecycleBoundary,
    private val renderingLifecycleBoundary: IntelligenceCommandRenderingLifecycleBoundary,
    private val screenInteractionBoundary: IntelligenceCommandScreenLifecycleInteractionBoundary,
    private val dispatcherProvider: CoroutineDispatcherProvider
) : ViewModel(), IntelligenceInteractionBoundary {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val activeJobs = mutableMapOf<IntelligenceUiInteraction, Job>()

    init {
        loadBriefing()
        observeFinality()
        observeAdoptedActions()
        observeMission()
    }

    private fun observeMission() {
        viewModelScope.launch {
            userContextRepository.getUserMission().collect { mission ->
                val currentState = _uiState.value
                if (currentState is HomeUiState.Success) {
                    _uiState.value = currentState.copy(userMission = mission)
                }
            }
        }
    }

    private fun observeFinality() {
        viewModelScope.launch {
            renderingLifecycleBoundary.lifecycleRenderingStream.collect { contract ->
                val currentState = _uiState.value
                if (currentState is HomeUiState.Success) {
                    _uiState.value = currentState.copy(finality = contract)
                }
            }
        }
    }

    private fun observeAdoptedActions() {
        viewModelScope.launch {
            getPreventionActionsUseCase().collect { actions ->
                val currentState = _uiState.value
                if (currentState is HomeUiState.Success) {
                    _uiState.value = currentState.copy(adoptedActions = actions.filter { it.isAdopted })
                }
            }
        }
    }

    fun handleUserInteraction(interaction: IntelligenceUiInteraction, onNavigate: (Any) -> Unit = {}) {
        screenInteractionBoundary.handleScreenInteraction(interaction, this, onNavigate)
    }

    override fun onInteraction(interaction: IntelligenceUiInteraction) {
        when (interaction) {
            is IntelligenceUiInteraction.RetryOperation -> retry(interaction)
            is IntelligenceUiInteraction.CancelOperation -> cancel(interaction.targetInteraction)
            else -> {}
        }
    }

    private fun cancel(target: IntelligenceUiInteraction) {
        activeJobs[target]?.let { job ->
            job.cancel()
            activeJobs.remove(target)
            resultHandoverBridge.routeToResult(target, IntelligenceCommandExecutionOutcome.Cancelled("cancel_home_${target.hashCode()}"))
        }
    }

    private fun retry(interaction: IntelligenceUiInteraction? = null) {
        loadBriefing(interaction)
    }

    private fun loadBriefing(interaction: IntelligenceUiInteraction? = null) {
        if (interaction != null) activeJobs[interaction]?.cancel()
        val job = viewModelScope.launch {
            interaction?.let { lifecycleBoundary.transitionTo(it, CommandLifecycleStage.PROCESSING) }
            _uiState.value = HomeUiState.Loading
            try {
                var briefing = getCurrentBriefingUseCase()
                if (briefing.items.isEmpty()) {
                    coordinator.runCycle()
                    briefing = getCurrentBriefingUseCase()
                }

                val reconciliations = withContext(dispatcherProvider.default) {
                    briefing.items.associate { item ->
                        item.intelligenceId to item.reconciliationContract?.toUiState().toContract()
                    }
                }

                val allSignals = briefing.signals.values.toList()
                val mission = userContextRepository.getUserMission().first()
                val adoptedActions = getPreventionActionsUseCase().first().filter { it.isAdopted }
                val healingLogs = getHealingLogUseCase().first()
                val truthChecks = healingRepository.getPatientTruthChecks().first()
                
                val watchedSignalsCount = allSignals.count { it.isWatched && !it.isActionTaken }
                val homeSignalsCount = allSignals.count { it.category == SignalCategory.ENVIRONMENT && "Home" in it.interactionContexts }
                
                val today = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()

                // Calculate Pillar Statuses based on Mission
                val pillars = if (mission == UserMission.HEALING) {
                    // ... (Healing pillars stay same)
                    // (I will keep the healing pillars code here for brevity)
                    val medicalAlerts = allSignals.filter { 
                        (it.category == SignalCategory.MEDICINE || it.category == SignalCategory.REGULATION) && 
                        (it.importance == SignalImportance.CRITICAL || it.importance == SignalImportance.HIGH) 
                    }
                    val treatmentDoneToday = healingLogs.any { 
                        it.type == HealingLogType.MANUAL && 
                        it.timestamp.atZone(ZoneId.systemDefault()).toLocalDate() == today 
                    }
                    val managedSymptoms = healingLogs.filter { 
                        it.type == HealingLogType.SYMPTOM && 
                        it.timestamp.atZone(ZoneId.systemDefault()).toLocalDate() == today 
                    }.map { it.directiveName }.distinct()

                    val scamCount = truthChecks.count { it.verdict == PatientVerdict.SCAM }

                    // Logic: Count available manuals + symptoms vs what was done today
                    val totalDirectivesToday = 1 + managedSymptoms.size // Base 1 treatment manual + active symptoms
                    val completedDirectivesToday = (if (treatmentDoneToday) 1 else 0) + managedSymptoms.size
                    val defenseScore = (completedDirectivesToday.toFloat() / totalDirectivesToday.toFloat()).coerceIn(0f, 1f)

                    listOf(
                        PillarStatus(id = "WATCH", title = "Sentinel Watch", status = if (medicalAlerts.isNotEmpty()) "${medicalAlerts.size} URGENT TREATMENT ALERTS" else "Treatment Integrity Clear", isAlert = medicalAlerts.isNotEmpty()),
                        PillarStatus(id = "TREATMENT", title = "Treatment", status = if (treatmentDoneToday) "Protocol Steps Completed" else "Next: Check Daily Manual"),
                        PillarStatus(id = "SYMPTOMS", title = "Symptom Help", status = if (managedSymptoms.isNotEmpty()) "Managing: ${managedSymptoms.size} Symptoms" else "Immediate Directives Ready"),
                        PillarStatus(id = "DECEPTION", title = "Deception Shield", status = "$scamCount Known Scams Blocked"),
                        PillarStatus(
                            id = "PROGRESS", 
                            title = "My Progress", 
                            status = "${(defenseScore * 100).toInt()}% DEFENSE REACHED",
                            progress = defenseScore
                        ),
                        PillarStatus(id = "VERIFY", title = "Laboratory", status = "Drug & Supplement Check")
                    )
                } else {
                    listOf(
                        PillarStatus("WATCH", "Sentinel Watch", "${allSignals.count { it.importance == SignalImportance.CRITICAL }} Urgent Alerts", allSignals.any { it.importance == SignalImportance.CRITICAL }),
                        PillarStatus("SHOP", "Shopping Shield", if (watchedSignalsCount > 0) "Watching $watchedSignalsCount items" else "200+ Chemicals Verified"),
                        PillarStatus("EAT", "Safe Eating", "Biological Blueprint"),
                        PillarStatus("TRUTH", "Health Claims", "Deception Shield"),
                        PillarStatus("HOME", "Safe Surroundings", if (homeSignalsCount > 0) "$homeSignalsCount Risks Monitored" else "Environment Hub"),
                        PillarStatus("ACADEMY", "Academy", "Intelligence Lessons"),
                        PillarStatus("PLAN", "Action Plan", "${adoptedActions.size} Habits Tracked")
                    )
                }

                val dailyPeace = if (mission == UserMission.HEALING) {
                    DailyPeace(
                        title = "You are Winning",
                        summary = "Every protective act you complete today strengthens your body's recovery shield.",
                        shield = "Focus on one directive at a time. Resilience is a pattern."
                    )
                } else {
                    DailyPeace(
                        title = "Microwaves are Safe",
                        summary = "The Agency has verified that non-ionizing waves are too weak to damage your DNA.",
                        shield = "Prevents unnecessary anxiety about common home appliances."
                    )
                }

                interaction?.let { resultHandoverBridge.routeToResult(it, IntelligenceCommandExecutionOutcome.Success("load_briefing_${briefing.id}")) }

                _uiState.value = HomeUiState.Success(
                    briefing = briefing,
                    userCountry = userContextRepository.getUserCountryCode(),
                    userMission = mission,
                    adoptedActions = adoptedActions,
                    dailyPeace = dailyPeace,
                    pillarStatuses = pillars,
                    reconciliations = reconciliations
                )
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) throw e
                interaction?.let { resultHandoverBridge.routeToResult(it, IntelligenceCommandExecutionOutcome.Failure(e, "load_briefing_failed")) }
                _uiState.value = HomeUiState.Error("Unable to load today's briefing.")
            } finally {
                interaction?.let { activeJobs.remove(it) }
            }
        }
        interaction?.let { activeJobs[it] = job }
    }
}
