package com.idontwantcancer.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.idontwantcancer.app.data.repository.BaselineIntelligenceSeeder
import com.idontwantcancer.app.data.worker.IntelligenceCycleScheduler
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class IwtlApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var intelligenceCycleScheduler: IntelligenceCycleScheduler

    @Inject
    lateinit var baselineSeeder: BaselineIntelligenceSeeder

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        intelligenceCycleScheduler.schedule()
        
        MainScope().launch {
            baselineSeeder.seedIfEmpty()
        }
    }
}
