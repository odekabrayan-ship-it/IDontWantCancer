package com.idontwantcancer.app.test

import com.idontwantcancer.app.core.concurrent.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
class TestCoroutineDispatcherProvider(
    dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
) : CoroutineDispatcherProvider {
    override val main: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
}
