package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceBriefingSnapshotTest {

    @Test
    fun `snapshot preserves exact state at time of creation`() {
        val now = Instant.now()
        val originalSignal = createMockSignal("sig1", SignificanceOutcome.CRITICAL)
        
        val briefing = IntelligenceBriefing(
            id = "b1",
            cycleId = "c1",
            generatedAt = now,
            status = BriefingStatus.READY,
            items = listOf(createMockBriefingItem("sig1")),
            signals = mapOf("sig1" to originalSignal),
            handoffs = mapOf("sig1" to createMockHandoff("sig1", originalSignal))
        )

        // Simulate serialization to snapshot
        val json = Json.encodeToString(briefing)
        
        // Simulate change in the "current" world
        val changedSignal = originalSignal.copy(significanceLevel = SignificanceOutcome.NOT_SIGNIFICANT)
        
        // Deserialize snapshot
        val reconstructedBriefing = Json.decodeFromString<IntelligenceBriefing>(json)

        // Verify snapshot still has the original value
        assertEquals(SignificanceOutcome.CRITICAL, reconstructedBriefing.signals["sig1"]?.significanceLevel)
        assertEquals(SignificanceOutcome.CRITICAL, reconstructedBriefing.handoffs["sig1"]?.communicationPackage?.significanceLevel)
    }

    private fun createMockSignal(id: String, significance: SignificanceOutcome) = Signal(
        id = id,
        title = "Title",
        summary = "Summary",
        category = SignalCategory.RESEARCH,
        importance = SignalImportance.HIGH,
        confidence = SignalConfidence.HIGH,
        significanceLevel = significance,
        detectedAt = Instant.now(),
        publishedAt = Instant.now(),
        source = SignalSource(name = "Source")
    )

    private fun createMockBriefingItem(signalId: String) = IntelligenceBriefingItem(
        id = "item-1",
        position = 0,
        communicationPackageId = signalId,
        intelligenceId = signalId,
        threadId = "t1",
        currentStateReference = "e1",
        changeReference = "c1",
        continuityReference = ContinuityLevel.CONTINUATION,
        significanceReference = SignificanceOutcome.SIGNIFICANT,
        priorityReference = AttentionLevel.ROUTINE,
        evidenceReference = EvidenceSynthesisLevel.SUPPORTED,
        uncertaintyReference = SignalConfidence.MODERATE,
        conflictReference = false,
        narrativeReference = "t1",
        provenanceReference = signalId,
        readiness = CommunicationReadinessLevel.READY,
        inclusionReason = "Reason"
    )

    private fun createMockHandoff(id: String, signal: Signal) = IntelligenceCommunicationHandoff(
        packageId = id,
        intelligenceId = id,
        threadId = "t1",
        communicationPackage = IntelligenceCommunicationPackage(
            intelligenceId = id,
            threadId = "t1",
            changeId = null,
            currentState = ReconstructedState("t1", "Summary", "e1", reconstructedAt = Instant.now()),
            continuity = null,
            narrative = IntelligenceNarrativeSequence("t1", "Topic", emptyList()),
            significanceLevel = signal.significanceLevel,
            priority = AttentionLevel.IMPORTANT,
            evidenceSynthesis = EvidenceSynthesisResult("t1", EvidenceSynthesisLevel.SUPPORTED, emptyList(), emptyList(), "Reason", Instant.now()),
            confidence = SignalConfidence.HIGH,
            conflicts = emptyList(),
            evidenceGap = EvidenceGapResult("t1", EvidenceGapLevel.NO_IDENTIFIED_GAP, "Reason", analyzedAt = Instant.now()),
            relevance = null,
            freshness = null,
            communicationReadiness = CommunicationReadinessResult(CommunicationReadinessLevel.READY, "R", CommunicationSafetyReason.SATEISFIED, false, Instant.now()),
            provenance = IntelligenceProvenance(
                signalId = id,
                signal = signal,
                thread = null,
                timelineEntries = emptyList(),
                sources = emptyList(),
                consolidatedEvent = null,
                reconstructedAt = Instant.now()
            ),
            assembledAt = Instant.now()
        ),
        authorizedAt = Instant.now()
    )
}
