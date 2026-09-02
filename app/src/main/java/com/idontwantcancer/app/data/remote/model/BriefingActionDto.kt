package com.idontwantcancer.app.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BriefingActionDto(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String
)
