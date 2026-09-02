package com.idontwantcancer.app.data.remote

import com.idontwantcancer.app.data.remote.model.BriefingResponseDto
import retrofit2.http.GET

interface BriefingApi {
    @GET("v1/briefing/current")
    suspend fun getCurrentBriefing(): BriefingResponseDto
}
