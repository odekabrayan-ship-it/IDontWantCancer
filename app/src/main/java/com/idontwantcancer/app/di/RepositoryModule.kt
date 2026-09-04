package com.idontwantcancer.app.di

import com.idontwantcancer.app.data.repository.BriefingRepositoryImpl
import com.idontwantcancer.app.data.repository.IntelligenceMemoryRepositoryImpl
import com.idontwantcancer.app.data.repository.PreventionRepositoryImpl
import com.idontwantcancer.app.data.repository.SignalRepositoryImpl
import com.idontwantcancer.app.data.repository.StaticIntelligenceSourceRegistry
import com.idontwantcancer.app.data.repository.UserContextRepositoryImpl
import com.idontwantcancer.app.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBriefingRepository(
        briefingRepositoryImpl: BriefingRepositoryImpl
    ): BriefingRepository

    @Binds
    @Singleton
    abstract fun bindSignalRepository(
        signalRepositoryImpl: SignalRepositoryImpl
    ): SignalRepository

    @Binds
    @Singleton
    abstract fun bindIntelligenceSourceRegistry(
        staticIntelligenceSourceRegistry: StaticIntelligenceSourceRegistry
    ): IntelligenceSourceRegistry

    @Binds
    @Singleton
    abstract fun bindIntelligenceMemoryRepository(
        intelligenceMemoryRepositoryImpl: IntelligenceMemoryRepositoryImpl
    ): IntelligenceMemoryRepository

    @Binds
    @Singleton
    abstract fun bindUserContextRepository(
        userContextRepositoryImpl: UserContextRepositoryImpl
    ): UserContextRepository

    @Binds
    @Singleton
    abstract fun bindPreventionRepository(
        preventionRepositoryImpl: PreventionRepositoryImpl
    ): PreventionRepository
}
