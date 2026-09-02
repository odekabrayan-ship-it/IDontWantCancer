package com.idontwantcancer.app.domain.model

import java.time.Instant

/**
 * The structured result of a communication package integrity check.
 */
data class IntelligenceCommunicationIntegrityResult(
    val packageId: String,
    val intelligenceId: String,
    val status: CommunicationIntegrityStatus,
    val violations: List<CommunicationIntegrityViolation>,
    val checkedAt: Instant
)
