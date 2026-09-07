package com.idontwantcancer.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idontwantcancer.app.domain.model.UserMission
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import com.idontwantcancer.app.domain.repository.UserContextRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val memoryRepository: IntelligenceMemoryRepository,
    private val userContextRepository: UserContextRepository
) : ViewModel() {

    val userMission: StateFlow<UserMission> = userContextRepository.getUserMission()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserMission.UNDEFINED
        )

    val userCountry: String = userContextRepository.getUserCountryCode()

    fun setMission(mission: UserMission) {
        viewModelScope.launch {
            userContextRepository.setUserMission(mission)
        }
    }

    fun clearIntelligenceMemory() {
        viewModelScope.launch {
            memoryRepository.clearAll()
        }
    }
}
