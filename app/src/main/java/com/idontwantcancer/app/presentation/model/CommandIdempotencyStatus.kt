package com.idontwantcancer.app.presentation.model

import kotlinx.serialization.Serializable

/**
 * Defines the status of a command idempotency check.
 */
@Serializable
enum class CommandIdempotencyStatus {
    /**
     * The command is unique or naturally repeatable; processing should proceed.
     */
    PROCEED,

    /**
     * The command has already been processed or is currently in progress; 
     * repeat effects should be avoided according to existing semantics.
     */
    DUPLICATE_DETECTED,

    /**
     * The command identity is recognized and its previous result should be replayed.
     */
    REUSE_EXISTING_RESULT
}
