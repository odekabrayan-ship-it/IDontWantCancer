package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a specific evidence-based prevention habit that a user can adopt.
 */
@Serializable
data class PreventionAction(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val isAdopted: Boolean = false
)
