package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * A Signal represents a meaningful change detected by the cancer intelligence system.
 */
@Serializable
data class Signal(
    val id: String,
    val title: String,
    val summary: String,
    val significance: String? = null,
    val significanceLevel: SignificanceOutcome? = null,
    val significanceFactors: List<String> = emptyList(),
    val explanation: String? = null,
    val category: SignalCategory,
    val importance: SignalImportance,
    val confidence: SignalConfidence,
    val confidenceFactors: List<EvidenceFactor> = emptyList(),
    val conflictStatus: ResolutionStatus? = null,
    @Serializable(with = InstantSerializer::class)
    val detectedAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val publishedAt: Instant,
    val recommendedAction: String? = null,
    val source: SignalSource,
    val supportingSources: List<SignalSource> = emptyList(),
    
    // Admission Context
    val lastAdmittedStateEntryId: String? = null
)
