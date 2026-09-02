package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines the nature of a detected disagreement between intelligence sources.
 */
@Serializable
enum class ConflictType {
    /**
     * Sources provide directly opposing factual claims for the same entity and scope.
     */
    DIRECT_CONTRADICTION,

    /**
     * Sources agree on the core change but disagree on specific details or extent.
     */
    PARTIAL_DISAGREEMENT,

    /**
     * Sources report the same event but with significantly different evidence levels.
     */
    EVIDENCE_MISMATCH,

    /**
     * The disagreement is explained by the passage of time (one state replaced another).
     */
    TEMPORAL_DIFFERENCE,

    /**
     * Sources appear to disagree because they are referring to different populations or contexts.
     */
    SCOPE_DIFFERENCE,

    /**
     * The sources are referring to different underlying entities/products.
     */
    ENTITY_DIFFERENCE,

    /**
     * The disagreement is explained by an explicit correction.
     */
    CORRECTION,

    /**
     * The disagreement is explained by an official supersession relationship.
     */
    SUPERSESSION,

    /**
     * The nature of the disagreement is not yet clear.
     */
    UNCERTAIN
}
