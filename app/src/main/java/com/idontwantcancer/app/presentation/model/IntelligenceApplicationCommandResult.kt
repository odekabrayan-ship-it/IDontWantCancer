package com.idontwantcancer.app.presentation.model

import com.idontwantcancer.app.domain.model.InstantSerializer
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * An immutable boundary model representing the result of an application-level command.
 * This component formalizes how authoritative outcomes return to the application state.
 */
@Serializable
sealed interface IntelligenceApplicationCommandResult {
    /**
     * The unique identifier of the operation, if available.
     */
    val operationId: String?

    /**
     * The timestamp when the result was produced.
     */
    @Serializable(with = InstantSerializer::class)
    val processedAt: Instant

    /**
     * Represents a successfully processed application command.
     */
    @Serializable
    data class Success(
        override val operationId: String? = null,
        @Serializable(with = InstantSerializer::class)
        override val processedAt: Instant = Instant.now()
    ) : IntelligenceApplicationCommandResult

    /**
     * Represents an application command that failed due to technical or logical errors.
     */
    @Serializable
    data class Failure(
        val reason: String,
        override val operationId: String? = null,
        @Serializable(with = InstantSerializer::class)
        override val processedAt: Instant = Instant.now()
    ) : IntelligenceApplicationCommandResult

    /**
     * Represents a command that was rejected by the authoritative domain rules.
     */
    @Serializable
    data class Rejected(
        val reason: String,
        override val operationId: String? = null,
        @Serializable(with = InstantSerializer::class)
        override val processedAt: Instant = Instant.now()
    ) : IntelligenceApplicationCommandResult

    /**
     * Represents a command that was explicitly cancelled.
     */
    @Serializable
    data class Cancelled(
        override val operationId: String? = null,
        @Serializable(with = InstantSerializer::class)
        override val processedAt: Instant = Instant.now()
    ) : IntelligenceApplicationCommandResult

    /**
     * Represents a command that exceeded its authoritative time limit.
     */
    @Serializable
    data class TimedOut(
        override val operationId: String? = null,
        @Serializable(with = InstantSerializer::class)
        override val processedAt: Instant = Instant.now()
    ) : IntelligenceApplicationCommandResult
}
