package com.idontwantcancer.app.presentation.healing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.HealingRepository
import com.idontwantcancer.app.domain.usecase.GetHealingLogUseCase
import com.idontwantcancer.app.domain.usecase.LogHealingActionUseCase
import com.idontwantcancer.app.presentation.boundary.*
import com.idontwantcancer.app.presentation.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealingViewModel @Inject constructor(
    private val healingRepository: HealingRepository,
    private val getHealingLogUseCase: GetHealingLogUseCase,
    private val logHealingActionUseCase: LogHealingActionUseCase,
    private val resultHandoverBridge: IntelligenceCommandExecutionResultHandoverBoundary,
    private val lifecycleBoundary: IntelligenceCommandLifecycleBoundary,
    private val screenInteractionBoundary: IntelligenceCommandScreenLifecycleInteractionBoundary
) : ViewModel(), IntelligenceInteractionBoundary {

    val uiState: StateFlow<HealingUiState> = combine(
        healingRepository.getTreatmentManuals(),
        healingRepository.getSymptomDirectives(),
        healingRepository.getPatientTruthChecks(),
        healingRepository.getRedFlagDirectives(),
        getHealingLogUseCase()
    ) { manuals, symptoms, truthChecks, redFlags, logs ->
        HealingUiState.Success(
            treatmentManuals = manuals,
            symptomDirectives = symptoms,
            patientTruthChecks = truthChecks,
            redFlagDirectives = redFlags,
            healingLogEntries = logs
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HealingUiState.Loading
    )

    fun logHealingAction(directiveId: String, name: String, type: HealingLogType) {
        viewModelScope.launch {
            logHealingActionUseCase(directiveId, name, type)
        }
    }

    override fun onInteraction(interaction: IntelligenceUiInteraction) {
        // Authoritative handler implementation
    }
}

sealed interface HealingUiState {
    data object Loading : HealingUiState
    data class Success(
        val treatmentManuals: List<TreatmentManual>,
        val symptomDirectives: List<SymptomDirective>,
        val patientTruthChecks: List<PatientTruthCheck>,
        val redFlagDirectives: List<RedFlagDirective>,
        val healingLogEntries: List<HealingLogEntry>
    ) : HealingUiState
}
