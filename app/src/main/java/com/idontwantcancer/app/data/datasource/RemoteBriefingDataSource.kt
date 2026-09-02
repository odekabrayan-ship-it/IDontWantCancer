package com.idontwantcancer.app.data.datasource

import com.idontwantcancer.app.data.remote.BriefingApi
import com.idontwantcancer.app.data.remote.model.BriefingResponseDto
import javax.inject.Inject

/**
 * Remote implementation of [BriefingDataSource] using [BriefingApi].
 */
class RemoteBriefingDataSource @Inject constructor(
    private val briefingApi: BriefingApi
) : BriefingDataSource {
    override suspend fun getCurrentBriefing(): BriefingResponseDto = briefingApi.getCurrentBriefing()
}
