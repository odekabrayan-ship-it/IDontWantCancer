package com.idontwantcancer.app.presentation.search

/**
 * Represents the Agency's immediate "Yes/No" judgment on a set of search results.
 */
sealed interface SummaryVerdict {
    data object NoHazardMatch : SummaryVerdict
    data class HazardDetected(val severity: String, val count: Int) : SummaryVerdict
    data class ScamDetected(val claim: String) : SummaryVerdict
}
