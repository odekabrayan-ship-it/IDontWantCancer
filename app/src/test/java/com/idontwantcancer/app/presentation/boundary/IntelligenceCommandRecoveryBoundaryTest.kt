package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.domain.engine.IntelligenceReentryRecoveryBoundary
import com.idontwantcancer.app.domain.model.ReentryRecoveryResult
import com.idontwantcancer.app.presentation.model.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class IntelligenceCommandRecoveryBoundaryTest {

    private val reentryRecoveryBoundary = mockk<IntelligenceReentryRecoveryBoundary>()
    private val boundary = DefaultIntelligenceCommandRecoveryBoundary(reentryRecoveryBoundary)

    @Test
    fun `reentry command failure delegates to Step 82 authority`() = runTest {
        val identity = "sig1::e2"
        val result = IntelligenceApplicationCommandResult.Failure("Fail", identity)
        
        coEvery { reentryRecoveryBoundary.getVerifiedReentry(identity) } returns 
            ReentryRecoveryResult.Verified(mockk(relaxed = true))

        val recoveryResult = boundary.evaluateRecovery(result)

        assertEquals(CommandRecoveryStatus.RECOVERED, recoveryResult.status)
    }

    @Test
    fun `generic command failure results in RECOVERY_NOT_REQUIRED`() = runTest {
        val result = IntelligenceApplicationCommandResult.Failure("Fail", "generic_op")

        val recoveryResult = boundary.evaluateRecovery(result)

        assertEquals(CommandRecoveryStatus.RECOVERY_NOT_REQUIRED, recoveryResult.status)
    }

    @Test
    fun `unverified reentry results in RECOVERY_FAILED`() = runTest {
        val identity = "sig1::e2"
        val result = IntelligenceApplicationCommandResult.Failure("Fail", identity)
        
        coEvery { reentryRecoveryBoundary.getVerifiedReentry(identity) } returns 
            ReentryRecoveryResult.Unverified(identity, null, "Integrity failure")

        val recoveryResult = boundary.evaluateRecovery(result)

        assertEquals(CommandRecoveryStatus.RECOVERY_FAILED, recoveryResult.status)
    }
}
