package com.idontwantcancer.app.di

import com.idontwantcancer.app.data.datasource.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindSignalDataSource(
        remoteSignalDataSource: RemoteSignalDataSource
    ): SignalDataSource

    @Binds
    @Singleton
    abstract fun bindBriefingDataSource(
        remoteBriefingDataSource: RemoteBriefingDataSource
    ): BriefingDataSource

    @Binds
    @Singleton
    abstract fun bindIntelligenceDataSource(
        delegatingIntelligenceDataSource: DelegatingIntelligenceDataSource
    ): IntelligenceDataSource

    @Binds
    @IntoSet
    abstract fun bindOpenFdaFoodSourceAdapter(
        openFdaFoodSourceAdapter: OpenFdaFoodSourceAdapter
    ): SourceAdapter
}
