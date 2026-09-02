package com.idontwantcancer.app.di

import com.idontwantcancer.app.data.remote.BriefingApi
import com.idontwantcancer.app.data.remote.SignalApi
import com.idontwantcancer.app.data.remote.fda.OpenFdaApi
import com.idontwantcancer.app.di.qualifier.AgencyApi
import com.idontwantcancer.app.di.qualifier.FdaApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.idontwantcancer.app/"
    private const val FDA_BASE_URL = "https://api.fda.gov/"

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    @AgencyApi
    fun provideAgencyRetrofit(json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    @FdaApi
    fun provideFdaRetrofit(json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(FDA_BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideBriefingApi(@AgencyApi retrofit: Retrofit): BriefingApi {
        return retrofit.create(BriefingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSignalApi(@AgencyApi retrofit: Retrofit): SignalApi {
        return retrofit.create(SignalApi::class.java)
    }

    @Provides
    @Singleton
    fun provideOpenFdaApi(@FdaApi retrofit: Retrofit): OpenFdaApi {
        return retrofit.create(OpenFdaApi::class.java)
    }
}
