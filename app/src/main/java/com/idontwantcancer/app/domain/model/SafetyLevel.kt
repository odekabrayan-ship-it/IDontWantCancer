package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the safety level assigned by the agency based on authoritative evidence.
 */
@Serializable
enum class SafetyLevel {
    /**
     * Current authoritative evidence suggests the item or substance is safe for intended use.
     */
    VERIFIED_SAFE,

    /**
     * Evidence is developing or inconclusive; users are advised to monitor for updates.
     */
    MONITOR,

    /**
     * Authoritative bodies have issued warnings or "possibly carcinogenic" classifications.
     */
    CAUTION,

    /**
     * Active recalls, "probably/known carcinogenic" classifications, or immediate hazards.
     */
    DANGER,

    /**
     * No safety level has been determined for this topic yet.
     */
    UNDEFINED
}
