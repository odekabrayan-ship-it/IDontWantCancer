package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * A coherent dataset representing the agency's current briefing for the user.
 * Assembled from the most significant and highest priority current signals.
 */
@Serializable
data class IntelligenceBriefing(
    val id: String,
    val cycleId: String,
    @Serializable(with = InstantSerializer::class)
    val generatedAt: Instant,
    val status: BriefingStatus,
    val items: List<IntelligenceBriefingItem>,
    
    // Registry of signals referenced by items
    val signals: Map<String, Signal>,
    
    // Registry of authorized handoff contracts for each item
    val handoffs: Map<String, IntelligenceCommunicationHandoff> = emptyMap(),
    
    // Derived action items for the user
    val actionItems: List<BriefingAction> = emptyList()
)
