package com.idontwantcancer.app.presentation.signal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idontwantcancer.app.core.concurrent.CoroutineDispatcherProvider
import com.idontwantcancer.app.domain.engine.IntelligenceReentryReconciliationConsumptionBoundary
import com.idontwantcancer.app.domain.usecase.GetSignalByIdUseCase
import com.idontwantcancer.app.domain.usecase.UpdateActionTakenStatusUseCase
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
 * ViewModel for the Signal Detail screen.
 *
 * Manages the presentation state of a specific cancer-intelligence signal.
 */
@HiltViewModel
class SignalDetailViewModel @Inject constructor(
    private val getSignalByIdUseCase: GetSignalByIdUseCase,
    private val updateActionTakenStatusUseCase: UpdateActionTakenStatusUseCase,
    private val reconciliationBoundary: IntelligenceReentryReconciliationConsumptionBoundary,
    private val resultHandoverBridge: IntelligenceCommandExecutionResultHandoverBoundary,
    private val lifecycleBoundary: IntelligenceCommandLifecycleBoundary,
    private val renderingLifecycleBoundary: IntelligenceCommandRenderingLifecycleBoundary,
    private val screenInteractionBoundary: IntelligenceCommandScreenLifecycleInteractionBoundary,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), IntelligenceInteractionBoundary {

    private val _uiState = MutableStateFlow<SignalDetailUiState>(SignalDetailUiState.Loading)
    val uiState: StateFlow<SignalDetailUiState> = _uiState.asStateFlow()

    private val activeJobs = mutableMapOf<IntelligenceUiInteraction, Job>()

    init {
        val signalId = savedStateHandle.get<String>("signalId")
        if (signalId != null) {
            loadSignal(signalId)
        }
        observeFinality()
    }

    private fun observeFinality() {
        viewModelScope.launch {
            renderingLifecycleBoundary.lifecycleRenderingStream.collect { contract ->
                val currentState = _uiState.value
                if (currentState is SignalDetailUiState.Success) {
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
        if (interaction is IntelligenceUiInteraction.AcknowledgeSignal) {
            toggleActionTaken(interaction.signalId)
            return
        }

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
            else -> {}
        }
    }

    private fun cancel(target: IntelligenceUiInteraction) {
        activeJobs[target]?.let { job ->
            job.cancel()
            activeJobs.remove(target)
            
            // Step 121 / 157 / 172 / 187: Report cancellation
            resultHandoverBridge.routeToResult(
                target, 
                IntelligenceCommandExecutionOutcome.Cancelled("cancel_detail_${target.hashCode()}")
            )
        }
    }

    /**
     * Triggers another attempt to load the signal.
     */
    private fun retry(interaction: IntelligenceUiInteraction? = null) {
        val currentState = _uiState.value
        when (currentState) {
            is SignalDetailUiState.Success -> loadSignal(currentState.signal.id, interaction)
            is SignalDetailUiState.Error -> {
                // If we errored but had an ID in SavedStateHandle, try again
                savedStateHandle.get<String>("signalId")?.let { loadSignal(it, interaction) }
            }
            else -> {}
        }
    }

    private fun toggleActionTaken(signalId: String) {
        val currentState = _uiState.value
        if (currentState is SignalDetailUiState.Success) {
            viewModelScope.launch {
                val newStatus = !currentState.signal.isActionTaken
                updateActionTakenStatusUseCase(signalId, newStatus)
                // Hot-patch the UI state for immediate feedback
                _uiState.value = currentState.copy(
                    signal = currentState.signal.copy(isActionTaken = newStatus)
                )
            }
        }
    }

    /**
     * Loads the signal with the provided identifier.
     *
     * @param signalId The unique identifier of the signal.
     * @param interaction The UI interaction that triggered the load.
     */
    fun loadSignal(signalId: String, interaction: IntelligenceUiInteraction? = null) {
        if (interaction != null) {
            activeJobs[interaction]?.cancel()
        }

        val job = viewModelScope.launch {
            // Step 113: PROCESSING
            interaction?.let { lifecycleBoundary.transitionTo(it, CommandLifecycleStage.PROCESSING) }

            _uiState.value = SignalDetailUiState.Loading
            try {
                val signal = getSignalByIdUseCase(signalId)
                if (signal != null) {
                    // Step 212: Move mapping to Default dispatcher
                    val reconciliation = withContext(dispatcherProvider.default) {
                        val reentryIdentity = "${signal.id}::${signal.lastAdmittedStateEntryId}"
                        reconciliationBoundary.getReconciliationContract(reentryIdentity)
                            ?.toUiState().toContract()
                    }
                    
                    // Step 112 / 157 / 172 / 187: Report success outcome
                    interaction?.let { 
                        resultHandoverBridge.routeToResult(
                            it, 
                            IntelligenceCommandExecutionOutcome.Success(operationId = "detail_$signalId")
                        )
                    }

                    _uiState.value = SignalDetailUiState.Success(signal, reconciliation)
                } else {
                    // Step 121 / 157 / 172 / 187: Report missing case as failure outcome
                    interaction?.let { 
                        resultHandoverBridge.routeToResult(
                            it, 
                        IntelligenceCommandExecutionOutcome.Failure(
                                Exception("Signal not found"), 
                                "detail_$signalId"
                            )
                        )
                    }

                    _uiState.value = SignalDetailUiState.NotFound
                }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) throw e

                // Step 117 / 157 / 172 / 187: Report failure outcome
                interaction?.let { 
                    resultHandoverBridge.routeToResult(
                        it, 
                        IntelligenceCommandExecutionOutcome.Failure(e, "detail_$signalId")
                    )
                }

                _uiState.value = SignalDetailUiState.Error("Unable to load this intelligence.")
            } finally {
                interaction?.let { activeJobs.remove(it) }
            }
        }
        interaction?.let { activeJobs[it] = job }
    }
}
