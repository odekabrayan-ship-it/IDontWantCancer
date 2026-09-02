package com.idontwantcancer.app.data.datasource

import com.idontwantcancer.app.data.mapper.toSourceMaterial
import com.idontwantcancer.app.data.remote.fda.OpenFdaApi
import com.idontwantcancer.app.domain.model.SourceMaterial
import javax.inject.Inject

/**
 * Adapter for the openFDA Food Enforcement API.
 * This adapter handles the specific search and normalization rules for FDA food recalls.
 */
class OpenFdaFoodSourceAdapter @Inject constructor(
    private val fdaApi: OpenFdaApi
) : SourceAdapter {

    override val sourceId: String = "fda_food"

    override suspend fun fetch(): List<SourceMaterial> {
        return try {
            // Retrieve the most recent Class I (highest risk) ongoing food recalls.
            // This search targets immediate health-related information changes.
            val response = fdaApi.getFoodEnforcement(
                search = "classification:\"Class I\" AND status:\"Ongoing\"",
                limit = 25
            )
            
            response.results.map { it.toSourceMaterial() }
        } catch (e: retrofit2.HttpException) {
            // openFDA returns 404 if no results match the search criteria.
            // This is an expected "quiet-day" scenario.
            if (e.code() == 404) {
                emptyList()
            } else {
                throw e
            }
        } catch (e: Exception) {
            // Rethrow or handle other network/parsing errors through the cycle coordinator.
            throw e
        }
    }
}
