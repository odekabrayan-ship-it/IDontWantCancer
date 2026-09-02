package com.idontwantcancer.app.data.remote.fda

import com.idontwantcancer.app.data.remote.model.fda.OpenFdaResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for the openFDA API.
 * Documentation: https://open.fda.gov/apis/food/enforcement/
 */
interface OpenFdaApi {
    /**
     * Retrieves food enforcement (recall) records.
     *
     * @param search The openFDA search query.
     * @param limit The number of records to return.
     * @return The response containing recall results.
     */
    @GET("food/enforcement.json")
    suspend fun getFoodEnforcement(
        @Query("search") search: String,
        @Query("limit") limit: Int = 10
    ): OpenFdaResponseDto
}
