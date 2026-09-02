package com.idontwantcancer.app.data.remote

import com.idontwantcancer.app.data.remote.model.SignalDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SignalApi {
    @GET("v1/signals/latest")
    suspend fun getLatestSignals(): List<SignalDto>

    @GET("v1/signals/attention")
    suspend fun getAttentionSignals(): List<SignalDto>

    @GET("v1/signals/search")
    suspend fun searchSignals(@Query("q") query: String): List<SignalDto>

    @GET("v1/signals/{id}")
    suspend fun getSignalById(@Path("id") id: String): SignalDto
}
