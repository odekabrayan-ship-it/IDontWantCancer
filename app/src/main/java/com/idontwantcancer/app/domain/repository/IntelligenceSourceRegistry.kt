package com.idontwantcancer.app.domain.repository

import com.idontwantcancer.app.domain.model.IntelligenceSource

/**
 * Registry of authoritative sources that the intelligence system monitors.
 */
interface IntelligenceSourceRegistry {
    /**
     * Retrieves all configured intelligence sources.
     */
    fun getSources(): List<IntelligenceSource>

    /**
     * Retrieves only the sources that are currently enabled for monitoring.
     */
    fun getEnabledSources(): List<IntelligenceSource>
}
