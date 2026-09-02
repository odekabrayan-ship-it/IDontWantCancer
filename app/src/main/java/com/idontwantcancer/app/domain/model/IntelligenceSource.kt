package com.idontwantcancer.app.domain.model

/**
 * Represents an authoritative source of cancer-related intelligence.
 */
data class IntelligenceSource(
    val id: String,
    val name: String,
    val type: IntelligenceSourceType,
    val authority: SourceAuthority,
    val reliability: SourceReliability,
    val operationalStatus: SourceOperationalStatus = SourceOperationalStatus.UNKNOWN,
    val isEnabled: Boolean = true
)
