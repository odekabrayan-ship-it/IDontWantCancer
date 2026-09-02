package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class BriefingAction(
    val title: String,
    val description: String
)
