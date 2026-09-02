package com.idontwantcancer.app.data.datasource

import com.idontwantcancer.app.data.remote.model.BriefingResponseDto

/**
 * Data source abstraction for retrieving daily briefings.
 */
interface BriefingDataSource {
    suspend fun getCurrentBriefing(): BriefingResponseDto
}
