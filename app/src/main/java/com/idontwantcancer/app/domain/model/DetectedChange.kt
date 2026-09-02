package com.idontwantcancer.app.domain.model

import java.time.Instant

/**
 * Represents a meaningful difference detected between versions of source material
 * or between current understanding and new information.
 */
data class DetectedChange(
    val id: String,
    val type: ChangeType,
    val sourceId: String,
    val contentId: String,
    val threadId: String? = null,
    val description: String,
    val detectedAt: Instant,
    val previousStateReference: String? = null,
    val currentStateReference: String? = null
)
