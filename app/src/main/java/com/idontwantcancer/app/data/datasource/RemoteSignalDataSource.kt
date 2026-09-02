package com.idontwantcancer.app.data.datasource

import com.idontwantcancer.app.data.remote.SignalApi
import com.idontwantcancer.app.data.remote.model.SignalDto
import javax.inject.Inject

/**
 * Remote implementation of [SignalDataSource] using [SignalApi].
 */
class RemoteSignalDataSource @Inject constructor(
    private val signalApi: SignalApi
) : SignalDataSource {
    override suspend fun getLatestSignals(): List<SignalDto> = signalApi.getLatestSignals()
    override suspend fun getAttentionSignals(): List<SignalDto> = signalApi.getAttentionSignals()
    override suspend fun searchSignals(query: String): List<SignalDto> = signalApi.searchSignals(query)
    override suspend fun getSignalById(signalId: String): SignalDto = signalApi.getSignalById(signalId)
}
