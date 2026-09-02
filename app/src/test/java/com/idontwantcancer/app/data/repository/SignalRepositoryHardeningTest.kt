package com.idontwantcancer.app.data.repository

import com.idontwantcancer.app.data.datasource.SignalDataSource
import com.idontwantcancer.app.data.local.dao.SignalDao
import com.idontwantcancer.app.data.remote.model.SignalDto
import com.idontwantcancer.app.test.TestCoroutineDispatcherProvider
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SignalRepositoryHardeningTest {

    private val dataSource = mockk<SignalDataSource>()
    private val dao = mockk<SignalDao>(relaxed = true)
    private val dispatcherProvider = TestCoroutineDispatcherProvider()
    private val repository = SignalRepositoryImpl(dataSource, dao, dispatcherProvider)

    @Test
    fun `getLatestSignals uses batch upsertAll for remote results`() = runTest {
        val dto = SignalDto(
            id = "1", 
            title = "T1", 
            summary = "S1",
            category = "FOOD",
            importance = "HIGH",
            confidence = "HIGH",
            detectedAt = "2023-01-01T00:00:00Z",
            publishedAt = "2023-01-01T00:00:00Z",
            sourceName = "Source"
        )
        val dtos = listOf(dto, dto.copy(id = "2"))
        coEvery { dataSource.getLatestSignals() } returns dtos

        repository.getLatestSignals()

        coVerify { dao.upsertAll(match { it.size == 2 }) }
        coVerify(exactly = 0) { dao.upsert(any()) }
    }
}
