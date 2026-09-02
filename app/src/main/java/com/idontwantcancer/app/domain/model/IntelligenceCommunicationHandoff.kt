package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * The authoritative, validated handoff contract between the intelligence domain 
 * and the communication layer. This model is immutable and contains only 
 * the structured context authorized for ordinary-person communication.
 */
@Serializable
data class IntelligenceCommunicationHandoff(
    val packageId: String,
    val intelligenceId: String,
    val threadId: String,
    
    // Validated hierarchy and context
    val communicationPackage: IntelligenceCommunicationPackage,
    
    // Controlled re-entry context if applicable
    val reentryHandoff: IntelligenceReentryHandoffResult? = null,
    
    // Metadata for the handoff
    @Serializable(with = InstantSerializer::class)
    val authorizedAt: Instant
)
