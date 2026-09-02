package com.idontwantcancer.app.data.datasource

import com.idontwantcancer.app.data.remote.model.SignalDto

/**
 * Data source abstraction for retrieving cancer-intelligence signals.
 */
interface SignalDataSource {
    suspend fun getLatestSignals(): List<SignalDto>
    suspend fun getAttentionSignals(): List<SignalDto>
    suspend fun searchSignals(query: String): List<SignalDto>
    suspend fun getSignalById(signalId: String): SignalDto
}
