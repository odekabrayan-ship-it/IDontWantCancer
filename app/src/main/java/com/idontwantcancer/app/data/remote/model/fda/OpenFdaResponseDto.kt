package com.idontwantcancer.app.data.remote.model.fda

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFdaResponseDto(
    @SerialName("results") val results: List<OpenFdaRecallDto>
)
