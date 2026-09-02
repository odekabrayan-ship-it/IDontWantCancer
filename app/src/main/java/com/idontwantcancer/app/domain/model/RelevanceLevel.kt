package com.idontwantcancer.app.domain.model

/**
 * Defines the determined relationship between a piece of intelligence 
 * and a specific context or thread.
 */
enum class RelevanceLevel {
    /**
     * The intelligence is a confirmed match for the context/thread.
     */
    RELEVANT,

    /**
     * The intelligence is not relevant to the specified context.
     */
    NOT_RELEVANT,

    /**
     * The intelligence concerns the same entity/topic but applies to a different scope.
     */
    SCOPE_MISMATCH,

    /**
     * The intelligence concerns a different underlying entity or subject.
     */
    ENTITY_MISMATCH,

    /**
     * There is insufficient structured context to establish relevance.
     */
    INSUFFICIENT_CONTEXT
}
