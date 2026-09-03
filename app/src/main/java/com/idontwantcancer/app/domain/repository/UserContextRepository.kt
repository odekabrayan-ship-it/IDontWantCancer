package com.idontwantcancer.app.domain.repository

/**
 * Repository for accessing the user's non-identifying environmental context.
 */
interface UserContextRepository {
    /**
     * Retrieves the user's current country code (ISO 3166-1 alpha-2).
     */
    fun getUserCountryCode(): String
}
