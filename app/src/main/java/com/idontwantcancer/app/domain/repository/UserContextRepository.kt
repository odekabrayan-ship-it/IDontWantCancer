package com.idontwantcancer.app.domain.repository

import com.idontwantcancer.app.domain.model.UserMission
import kotlinx.coroutines.flow.Flow

/**
 * Repository for accessing the user's non-identifying environmental context.
 */
interface UserContextRepository {
    /**
     * Retrieves the user's current country code (ISO 3166-1 alpha-2).
     */
    fun getUserCountryCode(): String

    /**
     * Retrieves the current mission of the user.
     */
    fun getUserMission(): Flow<UserMission>

    /**
     * Updates the user's mission.
     */
    suspend fun setUserMission(mission: UserMission)
}
