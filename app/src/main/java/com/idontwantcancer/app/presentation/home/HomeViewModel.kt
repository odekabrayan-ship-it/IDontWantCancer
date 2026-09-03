package com.idontwantcancer.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idontwantcancer.app.core.concurrent.CoroutineDispatcherProvider
import com.idontwantcancer.app.domain.engine.IntelligenceCycleCoordinator
import com.idontwantcancer.app.domain.usecase.GetCurrentBriefingUseCase
import com.idontwantcancer.app.presentation.boundary.*
import com.idontwantcancer.app.presentation.mapper.toContract
import com.idontwantcancer.app.presentation.mapper.toUiState
import com.idontwantcancer.app.presentation.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for the Home screen briefing.
 *
 * Manages the presentation state of the "What matters today?" briefing.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentBriefingUseCase: GetCurrentBriefingUseCase,
    private val coordinator: IntelligenceCycleCoordinator,
    private val resultHandoverBridge: IntelligenceCommandExecutionResultHandoverBoundary,
    private val lifecycleBoundary: IntelligenceCommandLifecycleBoundary,
    private val renderingLifecycleBoundary: IntelligenceCommandRenderingLifecycleBoundary,
    private val screenInteractionBoundary: IntelligenceCommandScreenLifecycleInteractionBoundary,
    private val dispatcherProvider: CoroutineDispatcherProvider
) : ViewModel(), IntelligenceInteractionBoundary {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Step 114: Track jobs for cancellation
    private val activeJobs = mutableMapOf<IntelligenceUiInteraction, Job>()

    init {
        loadBriefing()
        observeFinality()
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

    /**
     * Entry point for interactions originating from the screen (Step 199).
     */
    fun handleUserInteraction(
        interaction: IntelligenceUiInteraction,
        onNavigate: (Any) -> Unit = {}
    ) {
        // Step 199 Logic: Handover the screen-level gesture to the interaction authority.
        screenInteractionBoundary.handleScreenInteraction(
            interaction = interaction,
            handler = this,
            onNavigate = onNavigate
        )
    }

    override fun onInteraction(interaction: IntelligenceUiInteraction) {
        // Authoritative handler implementation (Step 141 / 156 / 169)
        when (interaction) {
            is IntelligenceUiInteraction.RetryOperation -> retry(interaction)
            is IntelligenceUiInteraction.CancelOperation -> cancel(interaction.targetInteraction)
            else -> {} // Navigation handled at composition level or through onNavigate callback
        }
    }

    private fun cancel(target: IntelligenceUiInteraction) {
        activeJobs[target]?.let { job ->
            job.cancel()
            activeJobs.remove(target)
            
            // Step 121 / 157 / 172 / 187: Report cancellation
            resultHandoverBridge.routeToResult(
                target, 
                IntelligenceCommandExecutionOutcome.Cancelled(operationId = "cancel_home_${target.hashCode()}")
            )
        }
    }

    /**
     * Triggers another attempt to load the current briefing.
     */
    private fun retry(interaction: IntelligenceUiInteraction? = null) {
        loadBriefing(interaction)
    }

    private fun loadBriefing(interaction: IntelligenceUiInteraction? = null) {
        // Only track lifecycle if an explicit interaction is provided
        if (interaction != null) {
            activeJobs[interaction]?.cancel()
        }

        val job = viewModelScope.launch {
            // Step 113: PROCESSING
            interaction?.let { lifecycleBoundary.transitionTo(it, CommandLifecycleStage.PROCESSING) }

            _uiState.value = HomeUiState.Loading
            try {
                var briefing = getCurrentBriefingUseCase()
                
                // Step 222: If briefing is empty, trigger an immediate autonomous cycle
                if (briefing.items.isEmpty()) {
                    coordinator.runCycle()
                    briefing = getCurrentBriefingUseCase()
                }

                // Step 212: Move expensive association and mapping to Default dispatcher
                val reconciliations = withContext(dispatcherProvider.default) {
                    briefing.items.associate { item ->
                        item.intelligenceId to item.reconciliationContract?.toUiState().toContract()
                    }
                }
                
                // Step 112 / 157 / 172 / 187: Report success outcome
                interaction?.let { 
                    resultHandoverBridge.routeToResult(
                        it, 
                        IntelligenceCommandExecutionOutcome.Success(operationId = "load_briefing_${briefing.id}")
                    )
                }

                _uiState.value = HomeUiState.Success(briefing, reconciliations)
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) throw e

                // Step 117 / 157 / 172 / 187: Report failure outcome
                interaction?.let { 
                    resultHandoverBridge.routeToResult(
                        it, 
                        IntelligenceCommandExecutionOutcome.Failure(e, "load_briefing_failed")
                    )
                }

                _uiState.value = HomeUiState.Error("Unable to load today's briefing.")
            } finally {
                interaction?.let { activeJobs.remove(it) }
            }
        }
        interaction?.let { activeJobs[it] = job }
    }
}
