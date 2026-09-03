package com.idontwantcancer.app.data.repository

import com.idontwantcancer.app.domain.repository.UserContextRepository
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserContextRepositoryImpl @Inject constructor() : UserContextRepository {
    override fun getUserCountryCode(): String {
        return Locale.getDefault().country ?: "US" // Default to US if unknown
    }
}
