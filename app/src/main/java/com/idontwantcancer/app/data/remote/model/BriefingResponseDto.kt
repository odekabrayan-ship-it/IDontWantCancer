package com.idontwantcancer.app.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BriefingResponseDto(
    @SerialName("id") val id: String,
    @SerialName("generated_at") val generatedAt: String,
    @SerialName("status") val status: String,
    @SerialName("headline") val headline: String,
    @SerialName("summary") val summary: String,
    @SerialName("important_signals") val importantSignals: List<SignalDto>,
    @SerialName("action_items") val actionItems: List<BriefingActionDto>
)
