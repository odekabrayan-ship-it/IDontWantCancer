package com.idontwantcancer.app.data.repository

import android.content.Context
import com.idontwantcancer.app.domain.model.UserMission
import com.idontwantcancer.app.domain.repository.UserContextRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserContextRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserContextRepository {
    
    private val prefs = context.getSharedPreferences("user_context", Context.MODE_PRIVATE)
    
    private val _mission = MutableStateFlow(loadMission())
    
    override fun getUserCountryCode(): String {
        return Locale.getDefault().country ?: "US"
    }

    override fun getUserMission(): Flow<UserMission> = _mission.asStateFlow()

    override suspend fun setUserMission(mission: UserMission) {
        prefs.edit().putString("user_mission", mission.name).apply()
        _mission.value = mission
    }

    private fun loadMission(): UserMission {
        val name = prefs.getString("user_mission", UserMission.UNDEFINED.name)
        return try {
            UserMission.valueOf(name!!)
        } catch (e: Exception) {
            UserMission.UNDEFINED
        }
    }
}
