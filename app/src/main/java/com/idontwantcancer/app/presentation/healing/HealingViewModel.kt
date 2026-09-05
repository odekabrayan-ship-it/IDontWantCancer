package com.idontwantcancer.app.presentation.healing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idontwantcancer.app.domain.model.TreatmentManual
import com.idontwantcancer.app.domain.repository.HealingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HealingViewModel @Inject constructor(
    private val healingRepository: HealingRepository
) : ViewModel() {

    val uiState: StateFlow<HealingUiState> = healingRepository.getTreatmentManuals()
        .map { manuals ->
            HealingUiState.Success(treatmentManuals = manuals)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HealingUiState.Loading
        )
}

sealed interface HealingUiState {
    data object Loading : HealingUiState
    data class Success(
        val treatmentManuals: List<TreatmentManual>
    ) : HealingUiState
}
