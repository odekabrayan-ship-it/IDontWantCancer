package com.idontwantcancer.app.data.datasource

import com.idontwantcancer.app.domain.model.SourceMaterial
import javax.inject.Inject

/**
 * Placeholder remote implementation of [IntelligenceDataSource].
 * Real network integration will be added in a later step.
 */
class RemoteIntelligenceDataSource @Inject constructor() : IntelligenceDataSource {
    override suspend fun fetchSourceMaterial(sourceId: String): List<SourceMaterial> {
        // Initially returns empty. This allows the cycle to run safely
        // without manufacturing fake data or making real network calls.
        return emptyList()
    }
}
