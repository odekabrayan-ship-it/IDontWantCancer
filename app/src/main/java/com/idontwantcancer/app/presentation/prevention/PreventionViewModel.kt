package com.idontwantcancer.app.presentation.prevention

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idontwantcancer.app.domain.engine.NutritionInstructionTransformer
import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.UserContextRepository
import com.idontwantcancer.app.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreventionViewModel @Inject constructor(
    getNutritionIntelligenceUseCase: GetNutritionIntelligenceUseCase,
    getEnvironmentalIntelligenceUseCase: GetEnvironmentalIntelligenceUseCase,
    getEducationLessonsUseCase: GetEducationLessonsUseCase,
    getPreventionActionsUseCase: GetPreventionActionsUseCase,
    private val toggleActionAdoptionUseCase: ToggleActionAdoptionUseCase,
    private val userContextRepository: UserContextRepository,
    private val transformer: NutritionInstructionTransformer
) : ViewModel() {

    val uiState: StateFlow<PreventionUiState> = combine(
        getNutritionIntelligenceUseCase(),
        getEnvironmentalIntelligenceUseCase(),
        getEducationLessonsUseCase(),
        getPreventionActionsUseCase()
    ) { nutrition, environmental, education, actions ->
        val countryCode = userContextRepository.getUserCountryCode()
        
        PreventionUiState.Success(
            dietaryPatterns = nutrition
                .filter { it.category == NutritionCategory.PATTERN }
                .map { item ->
                    item.copy(theExecution = transformer.transform(item.theExecution, countryCode))
                },
            preparationDirectives = nutrition
                .filter { it.category == NutritionCategory.PREPARATION }
                .map { item ->
                    item.copy(theExecution = transformer.transform(item.theExecution, countryCode))
                },
            environmentalSignals = environmental,
            educationLessons = education,
            preventionActions = actions,
            userCountry = countryCode
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PreventionUiState.Loading
    )

    fun toggleActionAdoption(id: String) {
        viewModelScope.launch {
            toggleActionAdoptionUseCase(id)
        }
    }
}

sealed interface PreventionUiState {
    data object Loading : PreventionUiState
    data class Success(
        val dietaryPatterns: List<NutritionIntelligence>,
        val preparationDirectives: List<NutritionIntelligence>,
        val environmentalSignals: List<Signal>,
        val educationLessons: List<EducationLesson>,
        val preventionActions: List<PreventionAction>,
        val userCountry: String
    ) : PreventionUiState
}
