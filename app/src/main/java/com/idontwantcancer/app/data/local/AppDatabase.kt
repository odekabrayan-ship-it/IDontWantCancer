package com.idontwantcancer.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.idontwantcancer.app.data.local.dao.ConflictDao
import com.idontwantcancer.app.data.local.dao.*
import com.idontwantcancer.app.data.local.entity.*

/**
 * Main database for the "I Don't Want Cancer" application.
 * Stores intelligence memory including source materials and evaluated signals.
 */
@Database(
    entities = [
        SourceMaterialEntity::class,
        SignalEntity::class,
        ConflictEntity::class,
        ThreadEntity::class,
        ConsolidatedEventEntity::class,
        TimelineEntryEntity::class,
        SupersessionRelationEntity::class,
        StateTransitionEntity::class,
        BriefingSnapshotEntity::class,
        ReentryLifecycleEntity::class,
        ReentryAuditEntity::class
    ],
    version = 13,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sourceMaterialDao(): SourceMaterialDao
    abstract fun signalDao(): SignalDao
    abstract fun conflictDao(): ConflictDao
    abstract fun threadDao(): ThreadDao
    abstract fun consolidatedEventDao(): ConsolidatedEventDao
    abstract fun timelineEntryDao(): TimelineEntryDao
    abstract fun supersessionRelationDao(): SupersessionRelationDao
    abstract fun stateTransitionDao(): StateTransitionDao
    abstract fun briefingSnapshotDao(): BriefingSnapshotDao
    abstract fun reentryLifecycleDao(): ReentryLifecycleDao
    abstract fun reentryAuditDao(): ReentryAuditDao

    companion object {
        const val DATABASE_NAME = "intelligence_memory.db"
    }
}
