package com.idontwantcancer.app.domain.model

import java.time.Instant

/**
 * The structured result of a lifecycle integrity check.
 */
data class IntelligenceIntegrityResult(
    val isValid: Boolean,
    val violations: List<IntegrityViolation>,
    val checkedAt: Instant
)
