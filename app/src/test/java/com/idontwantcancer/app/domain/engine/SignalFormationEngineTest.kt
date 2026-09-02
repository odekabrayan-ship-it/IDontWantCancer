package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class SignalFormationEngineTest {

    private val engine = DefaultSignalFormationEngine()

    @Test
    fun `when significance is CRITICAL, signal is CRITICAL`() = runTest {
        val source = createMockSource()
        val material = createMockMaterial()
        val significance = createMockSignificance(SignificanceOutcome.CRITICAL)
        val confidence = createMockConfidence(SignalConfidence.HIGH)

        val result = engine.formSignal(significance, confidence, emptyList(), source, material)

        assertTrue(result is SignalFormationResult.SignalCreated)
        val signal = (result as SignalFormationResult.SignalCreated).signal
        assertEquals(SignalImportance.CRITICAL, signal.importance)
        assertEquals(SignalConfidence.HIGH, signal.confidence)
    }

    @Test
    fun `when significance is SIGNIFICANT and source is national, signal is MODERATE`() = runTest {
        val source = createMockSource(authority = SourceAuthority.NATIONAL)
        val material = createMockMaterial()
        val significance = createMockSignificance(SignificanceOutcome.SIGNIFICANT)
        val confidence = createMockConfidence(SignalConfidence.MODERATE)

        val result = engine.formSignal(significance, confidence, emptyList(), source, material)

        assertTrue(result is SignalFormationResult.SignalCreated)
        val signal = (result as SignalFormationResult.SignalCreated).signal
        assertEquals(SignalImportance.MODERATE, signal.importance)
    }

    private fun createMockSource(
        authority: SourceAuthority = SourceAuthority.NATIONAL
    ) = IntelligenceSource(
        id = "test-source",
        name = "Test Agency",
        type = IntelligenceSourceType.PUBLIC_HEALTH,
        authority = authority,
        reliability = SourceReliability(
            trustLevel = SourceTrustLevel.MODERATE,
            isTransparent = true,
            updateReliability = SourceTrustLevel.MODERATE
        )
    )

    private fun createMockMaterial() = SourceMaterial(
        sourceId = "test-source",
        contentId = "id-123",
        title = "Title",
        content = "Content",
        publishedAt = Instant.now(),
        contentHash = "hash"
    )

    private fun createMockSignificance(outcome: SignificanceOutcome) = SignificanceDecision(
        outcome = outcome,
        reason = "Test Reason",
        factors = emptyList(),
        decidedAt = Instant.now()
    )

    private fun createMockConfidence(conf: SignalConfidence) = ConfidenceDecision(
        confidence = conf,
        factors = emptyList(),
        assessedAt = Instant.now()
    )
}
