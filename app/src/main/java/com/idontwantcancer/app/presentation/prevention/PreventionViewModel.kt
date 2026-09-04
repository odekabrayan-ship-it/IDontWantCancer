package com.idontwantcancer.app.presentation.prevention

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idontwantcancer.app.domain.model.EducationLesson
import com.idontwantcancer.app.domain.model.NutritionIntelligence
import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.domain.usecase.GetEducationLessonsUseCase
import com.idontwantcancer.app.domain.usecase.GetEnvironmentalIntelligenceUseCase
import com.idontwantcancer.app.domain.usecase.GetNutritionIntelligenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PreventionViewModel @Inject constructor(
    getNutritionIntelligenceUseCase: GetNutritionIntelligenceUseCase,
    getEnvironmentalIntelligenceUseCase: GetEnvironmentalIntelligenceUseCase,
    getEducationLessonsUseCase: GetEducationLessonsUseCase
) : ViewModel() {

    val uiState: StateFlow<PreventionUiState> = combine(
        getNutritionIntelligenceUseCase(),
        getEnvironmentalIntelligenceUseCase(),
        getEducationLessonsUseCase()
    ) { nutrition, environmental, education ->
        PreventionUiState.Success(
            nutritionTruths = nutrition,
            environmentalSignals = environmental,
            educationLessons = education
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PreventionUiState.Loading
    )
}

sealed interface PreventionUiState {
    data object Loading : PreventionUiState
    data class Success(
        val nutritionTruths: List<NutritionIntelligence>,
        val environmentalSignals: List<Signal>,
        val educationLessons: List<EducationLesson>
    ) : PreventionUiState
}
