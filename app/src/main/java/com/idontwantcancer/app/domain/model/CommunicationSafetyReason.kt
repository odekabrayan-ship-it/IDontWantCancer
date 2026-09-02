package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

/**
 * Defines structured reasons for communication safety gate results.
 */
@Serializable
enum class CommunicationSafetyReason {
    /**
     * The intelligence satisfies all structural requirements for communication.
     */
    SATEISFIED,

    /**
     * Required provenance/traceability information is missing.
     */
    MISSING_PROVENANCE,

    /**
     * Collective evidence state is not available or incomplete.
     */
    MISSING_EVIDENCE_STATE,

    /**
     * Authoritative current state is not available.
     */
    MISSING_CURRENT_STATE,

    /**
     * Required historical or interpretive context is missing.
     */
    MISSING_REQUIRED_CONTEXT,

    /**
     * Structural uncertainty representation is missing or invalid.
     */
    MISSING_UNCERTAINTY_REPRESENTATION,

    /**
     * The communication package is internally inconsistent or invalid.
     */
    INVALID_COMMUNICATION_PACKAGE,

    /**
     * The current intelligence state is not supported for ordinary-person communication.
     */
    UNSUPPORTED_INTELLIGENCE_STATE,

    /**
     * The intelligence scope is not supported or defined.
     */
    UNSUPPORTED_SCOPE
}
