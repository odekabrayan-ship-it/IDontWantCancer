package com.idontwantcancer.app.domain.model

/**
 * Represents the outcome of the signal formation process.
 */
sealed interface SignalFormationResult {
    /**
     * A meaningful intelligence signal was successfully created.
     */
    data class SignalCreated(val signal: Signal) : SignalFormationResult

    /**
     * The change was detected but not considered significant enough to become a signal.
     */
    data class NotSignificant(val reason: String) : SignalFormationResult

    /**
     * The evidence supporting the change is currently insufficient to form a signal.
     */
    data class InsufficientEvidence(val reason: String) : SignalFormationResult

    /**
     * The intelligence requires human review before a signal can be formed.
     */
    data class RequiresReview(val reason: String) : SignalFormationResult
}
