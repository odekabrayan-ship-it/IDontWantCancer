package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Represents a piece of raw information obtained from an authoritative source.
 */
@Serializable
data class SourceMaterial(
    val sourceId: String,
    val contentId: String,
    val title: String,
    val content: String,
    @Serializable(with = InstantSerializer::class)
    val publishedAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null,
    val canonicalUrl: String? = null,
    val contentHash: String
)
