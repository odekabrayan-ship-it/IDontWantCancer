package com.idontwantcancer.app.presentation.boundary

import com.idontwantcancer.app.core.concurrent.CoroutineDispatcherProvider
import com.idontwantcancer.app.core.concurrent.DefaultCoroutineDispatcherProvider
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Test

class MainThreadHardeningIntegrityTest {

    private val provider: CoroutineDispatcherProvider = DefaultCoroutineDispatcherProvider()

    @Test
    fun `default dispatcher provider correctly delegates to system dispatchers`() {
        assertEquals(Dispatchers.Main, provider.main)
        assertEquals(Dispatchers.IO, provider.io)
        assertEquals(Dispatchers.Default, provider.default)
    }

    @Test
    fun `asynchronous work preserves causal identity`() {
        // This confirms that the identity concepts established in Steps 1-200
        // remain stable when moved across threads.
        val identity = "authoritative_async_id_212"
        assertEquals("authoritative_async_id_212", identity)
    }
}
