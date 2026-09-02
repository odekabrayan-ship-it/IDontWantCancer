package com.idontwantcancer.app.data.datasource

import com.idontwantcancer.app.domain.model.SourceMaterial

/**
 * Interface for a specialized source adapter that knows how to fetch 
 * material from a specific external intelligence source.
 */
interface SourceAdapter {
    /**
     * The unique identifier of the source handled by this adapter.
     */
    val sourceId: String

    /**
     * Fetches newly available material from the source.
     *
     * @return A list of normalized source material.
     */
    suspend fun fetch(): List<SourceMaterial>
}
