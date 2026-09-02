package com.idontwantcancer.app.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignalDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("summary") val summary: String,
    @SerialName("significance") val significance: String? = null,
    @SerialName("explanation") val explanation: String? = null,
    @SerialName("category") val category: String,
    @SerialName("importance") val importance: String,
    @SerialName("confidence") val confidence: String,
    @SerialName("detected_at") val detectedAt: String,
    @SerialName("published_at") val publishedAt: String,
    @SerialName("recommended_action") val recommendedAction: String? = null,
    @SerialName("source_name") val sourceName: String,
    @SerialName("source_url") val sourceUrl: String? = null
)
