package com.idontwantcancer.app.presentation.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idontwantcancer.app.core.concurrent.CoroutineDispatcherProvider
import com.idontwantcancer.app.domain.engine.IntelligenceReentryReconciliationConsumptionBoundary
import com.idontwantcancer.app.domain.usecase.SearchSignalsUseCase
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
 * ViewModel for the Search screen.
 *
 * Manages the interaction with the intelligence search engine.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchSignalsUseCase: SearchSignalsUseCase,
    private val reconciliationBoundary: IntelligenceReentryReconciliationConsumptionBoundary,
    private val resultHandoverBridge: IntelligenceCommandExecutionResultHandoverBoundary,
    private val lifecycleBoundary: IntelligenceCommandLifecycleBoundary,
    private val renderingLifecycleBoundary: IntelligenceCommandRenderingLifecycleBoundary,
    private val screenInteractionBoundary: IntelligenceCommandScreenLifecycleInteractionBoundary,
    private val dispatcherProvider: CoroutineDispatcherProvider,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), IntelligenceInteractionBoundary {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var lastQuery: String
        get() = savedStateHandle.get<String>("query") ?: ""
        set(value) {
            savedStateHandle["query"] = value
        }

    // Step 114: Track in-flight jobs for cancellation
    private val activeJobs = mutableMapOf<IntelligenceUiInteraction, Job>()

    init {
        observeFinality()
        if (lastQuery.isNotBlank()) {
            search(lastQuery)
        }
    }

    private fun observeFinality() {
        viewModelScope.launch {
            renderingLifecycleBoundary.lifecycleRenderingStream.collect { contract ->
                val currentState = _uiState.value
                if (currentState is SearchUiState.Success) {
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
            is IntelligenceUiInteraction.PerformSearch -> search(interaction.query, interaction)
            is IntelligenceUiInteraction.ClearSearch -> clearSearch(interaction)
            is IntelligenceUiInteraction.RetryOperation -> retry(interaction)
            is IntelligenceUiInteraction.CancelOperation -> cancel(interaction.targetInteraction)
            is IntelligenceUiInteraction.ViewSignalDetails -> selectSignal(interaction.signalId)
            is IntelligenceUiInteraction.ClearSelection -> selectSignal(null)
            else -> {}
        }
    }

    private fun selectSignal(id: String?) {
        val currentState = _uiState.value
        if (currentState is SearchUiState.Success) {
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
                IntelligenceCommandExecutionOutcome.Cancelled(operationId = "cancel_${target.hashCode()}")
            )
        }
    }

    /**
     * Triggers another attempt to perform the last search.
     */
    private fun retry(interaction: IntelligenceUiInteraction? = null) {
        search(lastQuery, interaction)
    }

    /**
     * Performs an intelligence search for the provided query.
     *
     * @param query The search term.
     */
    private fun search(query: String, interaction: IntelligenceUiInteraction? = null) {
        lastQuery = query
        if (query.isBlank()) {
            clearSearch(interaction)
            return
        }

        if (interaction != null) {
            activeJobs[interaction]?.cancel()
        }

        val job = viewModelScope.launch {
            // Step 113: PROCESSING
            interaction?.let { lifecycleBoundary.transitionTo(it, CommandLifecycleStage.PROCESSING) }

            _uiState.value = SearchUiState.Searching
            try {
                val results = searchSignalsUseCase(query)
                
                // Step 121 / 157 / 172 / 187: Report success outcome
                interaction?.let { 
                    resultHandoverBridge.routeToResult(
                        it, 
                        IntelligenceCommandExecutionOutcome.Success(operationId = "search_${query.hashCode()}")
                    )
                }

                if (results.isEmpty()) {
                    _uiState.value = SearchUiState.Empty
                } else {
                    // Step 212: Move expensive association and mapping to Default dispatcher
                    val reconciliations = withContext(dispatcherProvider.default) {
                        results.associate { signal ->
                            val reentryIdentity = "${signal.id}::${signal.lastAdmittedStateEntryId}"
                            signal.id to reconciliationBoundary.getReconciliationContract(reentryIdentity)
                                ?.toUiState().toContract()
                        }
                    }
                    _uiState.value = SearchUiState.Success(results, reconciliations)
                }
            } catch (e: Exception) {
                if (e is kotlinx.coroutines.CancellationException) throw e
                
                // Step 117 / 157 / 172 / 187: Report failure outcome
                interaction?.let { 
                    resultHandoverBridge.routeToResult(
                        it, 
                        IntelligenceCommandExecutionOutcome.Failure(e, "search_failed")
                    )
                }
                
                _uiState.value = SearchUiState.Error("Unable to check the intelligence right now.")
            } finally {
                interaction?.let { activeJobs.remove(it) }
            }
        }
        interaction?.let { activeJobs[it] = job }
    }

    /**
     * Resets the search state to Idle.
     */
    private fun clearSearch(interaction: IntelligenceUiInteraction? = null) {
        // Cancel all search-related jobs
        activeJobs.values.forEach { it.cancel() }
        activeJobs.clear()

        // Step 121 / 157 / 172 / 187: Report success in resetting
        interaction?.let { 
            resultHandoverBridge.routeToResult(
                it, 
                IntelligenceCommandExecutionOutcome.Success(operationId = "clear_search")
            )
        }
        
        lastQuery = ""
        _uiState.value = SearchUiState.Idle
    }
}
