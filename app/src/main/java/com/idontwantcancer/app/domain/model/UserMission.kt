package com.idontwantcancer.app.domain.model

/**
 * Defines the primary mission of the user within the application.
 */
enum class UserMission {
    /**
     * User is focused on long-term cancer prevention and risk reduction.
     */
    PREVENTION,

    /**
     * User is actively fighting a cancer diagnosis and needs healing support.
     */
    HEALING,
    
    /**
     * Mission has not yet been selected (initial state).
     */
    UNDEFINED
}
