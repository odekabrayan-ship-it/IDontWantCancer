package com.idontwantcancer.app.domain.model

/**
 * Defines categories of intelligence lifecycle integrity violations.
 */
enum class IntegrityViolationCategory {
    /**
     * A required reference to another domain object is missing.
     */
    MISSING_REFERENCE,

    /**
     * A relationship exists between incompatible or unrelated entities.
     */
    INVALID_RELATIONSHIP,

    /**
     * Intelligence state contradicts its lifecycle or temporal context.
     */
    STATE_INCONSISTENCY,

    /**
     * Evidence support does not match the claimed intelligence state.
     */
    EVIDENCE_INCONSISTENCY,

    /**
     * Traceability or provenance data is missing or broken.
     */
    PROVENANCE_INCONSISTENCY,

    /**
     * Duplicate identifiers or records exist where uniqueness is required.
     */
    DUPLICATE_IDENTITY
}
