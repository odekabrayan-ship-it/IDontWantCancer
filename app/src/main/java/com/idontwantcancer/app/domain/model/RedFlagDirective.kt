package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a life-saving "Red Flag" directive for cancer patients.
 * Optimized for ordinary language and practical home-readiness.
 */
@Serializable
data class RedFlagDirective(
    val id: String,
    val title: String,
    val summary: String,
    val theTruth: String,
    val theCommand: String,
    val theExecution: List<String>,
    val whileYouWait: List<String>,
    val handoffScript: String,
    val theShield: String
)
