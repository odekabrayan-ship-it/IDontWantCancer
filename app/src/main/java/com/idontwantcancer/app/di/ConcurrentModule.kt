package com.idontwantcancer.app.di

import com.idontwantcancer.app.core.concurrent.CoroutineDispatcherProvider
import com.idontwantcancer.app.core.concurrent.DefaultCoroutineDispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ConcurrentModule {

    @Binds
    @Singleton
    abstract fun bindCoroutineDispatcherProvider(
        defaultCoroutineDispatcherProvider: DefaultCoroutineDispatcherProvider
    ): CoroutineDispatcherProvider
}
