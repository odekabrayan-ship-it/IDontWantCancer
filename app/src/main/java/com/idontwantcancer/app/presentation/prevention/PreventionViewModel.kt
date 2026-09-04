package com.idontwantcancer.app.presentation.prevention

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idontwantcancer.app.domain.model.NutritionIntelligence
import com.idontwantcancer.app.domain.usecase.GetNutritionIntelligenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PreventionViewModel @Inject constructor(
    getNutritionIntelligenceUseCase: GetNutritionIntelligenceUseCase
) : ViewModel() {

    val uiState: StateFlow<PreventionUiState> = getNutritionIntelligenceUseCase()
        .map { truths ->
            PreventionUiState.Success(nutritionTruths = truths)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PreventionUiState.Loading
        )
}

sealed interface PreventionUiState {
    data object Loading : PreventionUiState
    data class Success(
        val nutritionTruths: List<NutritionIntelligence>
    ) : PreventionUiState
}
