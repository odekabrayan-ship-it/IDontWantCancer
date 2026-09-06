package com.idontwantcancer.app.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idontwantcancer.app.domain.model.UserMission
import com.idontwantcancer.app.domain.repository.UserContextRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MissionSelectionViewModel @Inject constructor(
    private val userContextRepository: UserContextRepository
) : ViewModel() {

    fun selectMission(mission: UserMission) {
        viewModelScope.launch {
            userContextRepository.setUserMission(mission)
        }
    }
}
