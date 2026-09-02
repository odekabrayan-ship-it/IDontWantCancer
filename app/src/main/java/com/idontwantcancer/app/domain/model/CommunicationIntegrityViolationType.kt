package com.idontwantcancer.app.domain.model

/**
 * Defines categories of integrity violations within a communication package.
 */
enum class CommunicationIntegrityViolationType {
    IDENTITY_MISMATCH,
    THREAD_MISMATCH,
    CHANGE_MISMATCH,
    STATE_MISMATCH,
    CONTINUITY_MISMATCH,
    CONTEXT_MISSING,
    UNCERTAINTY_MISSING,
    EVIDENCE_MISMATCH,
    CONFLICT_MISSING,
    EVIDENCE_GAP_MISSING,
    PROVENANCE_MISSING,
    SUPERSESSION_MISMATCH,
    REQUIRED_FIELD_MISSING
}
