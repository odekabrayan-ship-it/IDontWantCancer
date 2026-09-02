package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommunicationOrderingContractTest {

    private val contract = DefaultIntelligenceCommunicationOrderingContract()

    @Test
    fun `URGENT ranks before HIGH`() {
        val h1 = createMockHandoff("sig1", AttentionLevel.IMPORTANT) // High priority
        val h2 = createMockHandoff("sig2", AttentionLevel.IMMEDIATE) // Urgent
        
        val list = listOf(h1, h2).sortedWith(contract.handoffComparator)

        assertEquals("sig2", list[0].intelligenceId)
    }

    @Test
    fun `identical priority uses significance as tie-breaker`() {
        // Higher significance comes first
        val h1 = createMockHandoff("sig1", AttentionLevel.IMPORTANT, SignificanceOutcome.SIGNIFICANT)
        val h2 = createMockHandoff("sig2", AttentionLevel.IMPORTANT, SignificanceOutcome.CRITICAL)

        val list = listOf(h1, h2).sortedWith(contract.handoffComparator)

        assertEquals("sig2", list[0].intelligenceId)
    }

    @Test
    fun `identical priority and significance uses recency as tie-breaker`() {
        val now = Instant.now()
        val hOlder = createMockHandoff("sig1", AttentionLevel.IMPORTANT, SignificanceOutcome.CRITICAL, now.minusSeconds(100))
        val hNewer = createMockHandoff("sig2", AttentionLevel.IMPORTANT, SignificanceOutcome.CRITICAL, now)

        val list = listOf(hOlder, hNewer).sortedWith(contract.handoffComparator)

        assertEquals("sig2", list[0].intelligenceId)
    }

    private fun createMockHandoff(
        id: String,
        priority: AttentionLevel,
        significance: SignificanceOutcome = SignificanceOutcome.SIGNIFICANT,
        detectedAt: Instant = Instant.now()
    ) = IntelligenceCommunicationHandoff(
        packageId = id,
        intelligenceId = id,
        threadId = "t1",
        communicationPackage = IntelligenceCommunicationPackage(
            intelligenceId = id,
            threadId = "t1",
            changeId = null,
            currentState = mockk(relaxed = true),
            continuity = null,
            narrative = mockk(relaxed = true),
            significanceLevel = significance,
            priority = priority,
            evidenceSynthesis = mockk(relaxed = true),
            confidence = SignalConfidence.HIGH,
            conflicts = emptyList(),
            evidenceGap = mockk(relaxed = true),
            relevance = null,
            freshness = null,
            communicationReadiness = mockk(relaxed = true),
            provenance = IntelligenceProvenance(
                signalId = id,
                signal = Signal(
                    id = id,
                    title = "Title",
                    summary = "Summary",
                    category = SignalCategory.RESEARCH,
                    importance = SignalImportance.HIGH,
                    confidence = SignalConfidence.HIGH,
                    significanceLevel = significance,
                    detectedAt = detectedAt,
                    publishedAt = Instant.now(),
                    source = SignalSource(name = "Source")
                ),
                thread = mockk(relaxed = true),
                timelineEntries = emptyList(),
                sources = listOf(mockk(relaxed = true)),
                consolidatedEvent = null,
                reconstructedAt = Instant.now()
            ),
            assembledAt = Instant.now()
        ),
        authorizedAt = Instant.now()
    )
}
