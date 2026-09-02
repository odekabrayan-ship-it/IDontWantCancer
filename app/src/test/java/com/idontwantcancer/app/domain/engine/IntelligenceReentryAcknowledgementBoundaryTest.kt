package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class IntelligenceReentryAcknowledgementBoundaryTest {

    private val boundary = DefaultIntelligenceReentryAcknowledgementBoundary()

    @Test
    fun `successful consumer processing produces acknowledgement`() {
        val acknowledgement = IntelligenceReentryConsumerAcknowledgement(
            reentryIdentity = "sig1::e2",
            consumer = IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY,
            status = ReentryAcknowledgementStatus.ACCEPTED,
            timestamp = Instant.now()
        )

        val result = boundary.acknowledge(acknowledgement)

        assertTrue(result.isRegistered)
        assertEquals(acknowledgement, result.acknowledgement)
    }

    @Test
    fun `failed consumer processing produces rejection acknowledgement`() {
        val acknowledgement = IntelligenceReentryConsumerAcknowledgement(
            reentryIdentity = "sig1::e2",
            consumer = IntelligenceConsumerIdentity.BRIEFING_ASSEMBLY,
            status = ReentryAcknowledgementStatus.REJECTED,
            timestamp = Instant.now(),
            reason = "Incompatible destination"
        )

        val result = boundary.acknowledge(acknowledgement)

        assertTrue(result.isRegistered)
        assertEquals(ReentryAcknowledgementStatus.REJECTED, result.acknowledgement.status)
    }
}
