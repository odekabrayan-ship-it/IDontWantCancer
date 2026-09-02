package com.idontwantcancer.app.data.datasource

import com.idontwantcancer.app.domain.model.SourceMaterial

/**
 * Data source abstraction for retrieving raw material from intelligence sources.
 */
interface IntelligenceDataSource {
    /**
     * Retrieves available material from a specific source.
     *
     * @param sourceId The identifier of the intelligence source to check.
     * @return A list of newly acquired source material.
     */
    suspend fun fetchSourceMaterial(sourceId: String): List<SourceMaterial>
}
