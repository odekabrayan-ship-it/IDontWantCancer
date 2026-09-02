package com.idontwantcancer.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val memoryRepository: IntelligenceMemoryRepository
) : ViewModel() {

    fun clearIntelligenceMemory() {
        viewModelScope.launch {
            memoryRepository.clearAll()
        }
    }
}
