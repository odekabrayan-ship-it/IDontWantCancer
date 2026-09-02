package com.idontwantcancer.app.presentation.alerts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idontwantcancer.app.core.concurrent.CoroutineDispatcherProvider
import com.idontwantcancer.app.domain.engine.IntelligenceReentryReconciliationConsumptionBoundary
import com.idontwantcancer.app.domain.usecase.GetAttentionSignalsUseCase
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
 * ViewModel for the Alerts screen.
 *
 * Manages the priority list of intelligence signals requiring attention.
 */
@HiltViewModel
class AlertsViewModel @Inject constructor(
    private val getAttentionSignalsUseCase: GetAttentionSignalsUseCase,
    private val reconciliationBoundary: IntelligenceReentryReconciliationConsumptionBoundary,
    private val resultHandoverBridge: IntelligenceCommandExecutionResultHandoverBoundary,
    private val lifecycleBoundary: IntelligenceCommandLifecycleBoundary,
    private val renderingLifecycleBoundary: IntelligenceCommandRenderingLifecycleBoundary,
    private val screenInteractionBoundary: IntelligenceCommandScreenLifecycleInteractionBoundary,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), IntelligenceInteractionBoundary {

    private val _uiState = MutableStateFlow<AlertsUiState>(AlertsUiState.Loading)
    val uiState: StateFlow<AlertsUiState> = _uiState.asStateFlow()

    private var selectedSignalId: String?
        get() = savedStateHandle.get<String>("selectedId")
        set(value) {
            savedStateHandle["selectedId"] = value
        }

    private val activeJobs = mutableMapOf<IntelligenceUiInteraction, Job>()

    init {
        loadAlerts()
        observeFinality()
    }

    private fun observeFinality() {
        viewModelScope.launch {
            renderingLifecycleBoundary.lifecycleRenderingStream.collect { contract ->
                val currentState = _uiState.value
                if (currentState is AlertsUiState.Success) {
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
        // Step 199 Logic: Handover screen interaction to the lifecycle interaction boundary.
        screenInteractionBoundary.handleScreenInteraction(
            interaction = interaction,
            handler = this,
            onNavigate = onNavigate
        )
    }

    override fun onInteraction(interaction: IntelligenceUiInteraction) {
        when (interaction) {
            is IntelligenceUiInteraction.RetryOperation -> retry(interaction)
            is IntelligenceUiInteraction.CancelOperation -> cancel(interaction.targetInteraction)
            is IntelligenceUiInteraction.ViewSignalDetails -> selectSignal(interaction.signalId)
            is IntelligenceUiInteraction.ClearSelection -> selectSignal(null)
            else -> {}
        }
    }

    private fun selectSignal(id: String?) {
        selectedSignalId = id
        val currentState = _uiState.value
        if (currentState is AlertsUiState.Success) {
            _uiState.value = currentState.copy(selectedSignalId = id)
        }
    }

    private fun cancel(target: IntelligenceUiInteraction) {
        activeJobs[target]?.let { job ->
            job.cancel()
            activeJobs.remove(target)
            
            // Step 121 / 157 / 172 / 187: Report cancellation
            resultHandoverBridge.routeToResult(
                target, 
                IntelligenceCommandExecutionOutcome.Cancelled("cancel_alerts_${target.hashCode()}")
            )
        }
    }

    /**
     * Triggers another attempt to load attention signals.
     */
    private fun retry(interaction: IntelligenceUiInteraction? = null) {
        loadAlerts(interaction)
    }

    private fun loadAlerts(interaction: IntelligenceUiInteraction? = null) {
        if (interaction != null) {
            activeJobs[interaction]?.cancel()
        }

        val job = viewModelScope.launch {
            // Step 113: PROCESSING
            interaction?.let { lifecycleBoundary.transitionTo(it, CommandLifecycleStage.PROCESSING) }

            _uiState.value = AlertsUiState.Loading
            try {
                val signals = getAttentionSignalsUseCase()
                
                // Step 212: Move association and mapping to Default dispatcher
                val reconciliations = withContext(dispatcherProvider.default) {
                    signals.associate { signal ->
                        val reentryIdentity = "${signal.id}::${signal.lastAdmittedStateEntryId}"
                        signal.id to reconciliationBoundary.getReconciliationContract(reentryIdentity)
                            ?.toUiState().toContract()
                    }
                }
                
                // Step 112 / 157 / 172 / 187: Report success outcome
                interaction?.let { 
                    resultHandoverBridge.routeToResult(
                        it, 
                        IntelligenceCommandExecutionOutcome.Success(operationId = "alerts_load")
                    )
                }

                _uiState.value = AlertsUiState.Success(signals, reconciliations, selectedSignalId = selectedSignalId)
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) throw e

                // Step 117 / 157 / 172 / 187: Report failure outcome
                interaction?.let { 
                    resultHandoverBridge.routeToResult(
                        it, 
                        IntelligenceCommandExecutionOutcome.Failure(e, "alerts_load")
                    )
                }

                // Catch failures at the ViewModel boundary and expose a user-safe message.
                _uiState.value = AlertsUiState.Error("Unable to load alerts.")
            } finally {
                interaction?.let { activeJobs.remove(it) }
            }
        }
        interaction?.let { activeJobs[it] = job }
    }
}
