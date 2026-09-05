package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a specialized protective directive for managing cancer treatment side effects.
 * Follows the 4-Point Directive Protocol: Truth, Command, Execution, Shield.
 */
@Serializable
data class SymptomDirective(
    val id: String,
    val name: String,
    val iconName: String,
    val theTruth: String,
    val theCommand: String,
    val theExecution: List<String>,
    val theShield: String
)
