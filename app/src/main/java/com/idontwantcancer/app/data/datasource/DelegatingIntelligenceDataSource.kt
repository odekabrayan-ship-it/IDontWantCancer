package com.idontwantcancer.app.data.datasource

import com.idontwantcancer.app.domain.model.SourceMaterial
import javax.inject.Inject

/**
 * An [IntelligenceDataSource] that delegates fetching logic to a set of specialized [SourceAdapter]s.
 */
class DelegatingIntelligenceDataSource @Inject constructor(
    private val adapters: Set<@JvmSuppressWildcards SourceAdapter>
) : IntelligenceDataSource {

    override suspend fun fetchSourceMaterial(sourceId: String): List<SourceMaterial> {
        val adapter = adapters.find { it.sourceId == sourceId }
        return adapter?.fetch() ?: emptyList()
    }
}
