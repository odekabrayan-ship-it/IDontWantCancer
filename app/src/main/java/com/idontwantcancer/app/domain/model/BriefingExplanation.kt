package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * A structured, deterministic explanation of an intelligence item.
 * Designed to be presentation-ready for the user while remaining 
 * strictly derived from domain-established intelligence.
 */
@Serializable
data class BriefingExplanation(
    val whatChanged: String,
    val whyItMatters: String,
    val evidenceStatus: String,
    val uncertainty: String?,
    val conflictSummary: String? = null
)
