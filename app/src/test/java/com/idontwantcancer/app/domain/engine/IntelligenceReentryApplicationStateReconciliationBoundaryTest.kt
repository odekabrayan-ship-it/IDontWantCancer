package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.BriefingRepository
import com.idontwantcancer.app.domain.repository.IntelligenceMemoryRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceReentryApplicationStateReconciliationBoundaryTest {

    private val memory = mockk<IntelligenceMemoryRepository>()
    private val briefingRepository = mockk<BriefingRepository>()
    private val boundary = DefaultIntelligenceReentryApplicationStateReconciliationBoundary(memory, briefingRepository)

    @Test
    fun `consistent state produces CONSISTENT result`() = runTest {
        val identity = "sig1::e2"
        val now = Instant.now()
        val completion = createMockCompletion(identity, ReentryTransitionCompletionStatus.COMPLETED)
        
        val signal = createMockSignal("sig1", "e2")
        val briefing = createMockBriefing("sig1", identity)

        coEvery { memory.getSignalById("sig1") } returns signal
        coEvery { briefingRepository.getCurrentBriefing() } returns briefing

        val result = boundary.reconcileWithApplication(completion)

        assertEquals(ApplicationStateReconciliationStatus.CONSISTENT, result.status)
    }

    @Test
    fun `signal mismatch produces INCONSISTENT result`() = runTest {
        val identity = "sig1::e2"
        val completion = createMockCompletion(identity, ReentryTransitionCompletionStatus.COMPLETED)
        
        // Signal points to wrong state
        val signal = createMockSignal("sig1", "e1") 
        val briefing = createMockBriefing("sig1", identity)

        coEvery { memory.getSignalById("sig1") } returns signal
        coEvery { briefingRepository.getCurrentBriefing() } returns briefing

        val result = boundary.reconcileWithApplication(completion)

        assertEquals(ApplicationStateReconciliationStatus.INCONSISTENT, result.status)
    }

    private fun createMockCompletion(id: String, status: ReentryTransitionCompletionStatus) = 
        IntelligenceReentryTransitionCompletionResult(
            reentryIdentity = id,
            status = status,
            verificationResult = IntelligenceReentryPostTransitionVerificationResult(
                reentryIdentity = id,
                status = ReentryPostTransitionVerificationStatus.VERIFIED,
                expectedState = ReentryLifecycleState.COMPLETED,
                actualState = ReentryLifecycleState.COMPLETED,
                verifiedAt = Instant.now()
            ),
            completedAt = Instant.now()
        )

    private fun createMockSignal(id: String, stateId: String) = Signal(
        id = id,
        title = "Title",
        summary = "Summ",
        category = SignalCategory.RESEARCH,
        importance = SignalImportance.HIGH,
        confidence = SignalConfidence.HIGH,
        detectedAt = Instant.now(),
        publishedAt = Instant.now(),
        source = SignalSource(name = "Source"),
        lastAdmittedStateEntryId = stateId
    )

    private fun createMockBriefing(signalId: String, reentryIdentity: String) = IntelligenceBriefing(
        id = "b1",
        cycleId = "c1",
        generatedAt = Instant.now(),
        status = BriefingStatus.READY,
        items = listOf(
            IntelligenceBriefingItem(
                id = "item1",
                position = 0,
                communicationPackageId = "p1",
                intelligenceId = signalId,
                threadId = "t1",
                currentStateReference = "e2",
                changeReference = null,
                continuityReference = null,
                significanceReference = SignificanceOutcome.SIGNIFICANT,
                priorityReference = AttentionLevel.ROUTINE,
                evidenceReference = EvidenceSynthesisLevel.SUPPORTED,
                uncertaintyReference = SignalConfidence.HIGH,
                conflictReference = false,
                narrativeReference = "t1",
                provenanceReference = "p1",
                inclusionReason = "R",
                explanation = null,
                readiness = CommunicationReadinessLevel.READY,
                reentryContract = IntelligenceReentryHandoffConsumptionContract(
                    reentryIdentity = reentryIdentity,
                    verifiedState = ReentryLifecycleState.COMPLETED,
                    intelligenceId = signalId,
                    stateEntryId = "e2",
                    transitionReason = null,
                    admittedAt = Instant.now(),
                    lastTransitionAt = Instant.now(),
                    isTerminal = true
                )
            )
        ),
        signals = emptyMap(),
        handoffs = emptyMap()
    )
}
