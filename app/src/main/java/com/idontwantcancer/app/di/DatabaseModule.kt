package com.idontwantcancer.app.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.idontwantcancer.app.data.local.AppDatabase
import com.idontwantcancer.app.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        val migration1To2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `conflicts` (`id` TEXT NOT NULL, `topicIdentifier` TEXT NOT NULL, `participatingSourceIds` TEXT NOT NULL, `type` TEXT NOT NULL, `resolutionStatus` TEXT NOT NULL, `competingSignalIds` TEXT NOT NULL, `detectedAt` INTEGER NOT NULL, `resolvedAt` INTEGER, `resolutionReasoning` TEXT, PRIMARY KEY(`id`))"
                )
            }
        }

        val migration2To3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `threads` (`id` TEXT NOT NULL, `topicIdentifier` TEXT NOT NULL, `firstDetectedAt` INTEGER NOT NULL, `lastUpdatedAt` INTEGER NOT NULL, `currentStatus` TEXT, `signalIds` TEXT NOT NULL, PRIMARY KEY(`id`))"
                )
            }
        }

        val migration3To4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `consolidated_events` (`id` TEXT NOT NULL, `topicIdentifier` TEXT NOT NULL, `sourceMaterialIds` TEXT NOT NULL, `firstDetectedAt` INTEGER NOT NULL, `lastUpdatedAt` INTEGER NOT NULL, `threadId` TEXT, PRIMARY KEY(`id`))"
                )
            }
        }

        val migration4To5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `supportingSourcesJson` TEXT NOT NULL DEFAULT '[]'"
                )
            }
        }

        val migration5To6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `timeline_entries` (`id` TEXT NOT NULL, `threadId` TEXT NOT NULL, `type` TEXT NOT NULL, `description` TEXT NOT NULL, `eventTime` INTEGER, `sourceTime` INTEGER, `publicationTime` INTEGER, `ingestionTime` INTEGER NOT NULL, `sourceMaterialId` TEXT, `changeId` TEXT, `signalId` TEXT, `stateSnapshot` TEXT, PRIMARY KEY(`id`))"
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_timeline_entries_threadId` ON `timeline_entries` (`threadId`)")
            }
        }

        val migration6To7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `supersession_relations` (`id` TEXT NOT NULL, `previousEntryId` TEXT NOT NULL, `supersedingEntryId` TEXT NOT NULL, `type` TEXT NOT NULL, `reason` TEXT NOT NULL, `detectedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))"
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_supersession_relations_previousEntryId` ON `supersession_relations` (`previousEntryId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_supersession_relations_supersedingEntryId` ON `supersession_relations` (`supersedingEntryId`)")
            }
        }

        val migration7To8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `state_transitions` (`id` TEXT NOT NULL, `threadId` TEXT NOT NULL, `previousStateEntryId` TEXT, `resultingStateEntryId` TEXT NOT NULL, `type` TEXT NOT NULL, `triggeringEventId` TEXT NOT NULL, `effectiveAt` INTEGER NOT NULL, `reason` TEXT NOT NULL, PRIMARY KEY(`id`))"
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_state_transitions_threadId` ON `state_transitions` (`threadId`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_state_transitions_triggeringEventId` ON `state_transitions` (`triggeringEventId`)")
            }
        }

        val migration8To9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `briefing_snapshots` (`id` TEXT NOT NULL, `cycleId` TEXT NOT NULL, `generatedAt` INTEGER NOT NULL, `status` TEXT NOT NULL, `briefingJson` TEXT NOT NULL, PRIMARY KEY(`id`))"
                )
            }
        }

        val migration9To10 = object : Migration(9, 10) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `lastAdmittedStateEntryId` TEXT"
                )
            }
        }

        val migration10To11 = object : Migration(10, 11) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `reentry_lifecycle` (`reentryIdentity` TEXT NOT NULL, `currentState` TEXT NOT NULL, `intelligenceId` TEXT NOT NULL, `stateEntryId` TEXT, `admittedAt` INTEGER NOT NULL, `lastTransitionAt` INTEGER NOT NULL, `transitionReason` TEXT, PRIMARY KEY(`reentryIdentity`))"
                )
            }
        }

        val migration11To12 = object : Migration(11, 12) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `reentry_audit` (`reentryIdentity` TEXT NOT NULL, `sequenceNumber` INTEGER NOT NULL, `previousState` TEXT, `newState` TEXT NOT NULL, `transitionReason` TEXT, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`reentryIdentity`, `sequenceNumber`))"
                )
            }
        }

        val migration12To13 = object : Migration(12, 13) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `isActionable` INTEGER NOT NULL DEFAULT 0"
                )
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `actionType` TEXT NOT NULL DEFAULT 'NONE'"
                )
            }
        }

        val migration13To14 = object : Migration(13, 14) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `verdict` TEXT"
                )
            }
        }

        val migration14To15 = object : Migration(14, 15) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `scope` TEXT NOT NULL DEFAULT 'GLOBAL'"
                )
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `targetCountryCode` TEXT"
                )
            }
        }

        val migration15To16 = object : Migration(15, 16) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `affectedIngredientsJson` TEXT NOT NULL DEFAULT '[]'"
                )
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `safetyLevel` TEXT NOT NULL DEFAULT 'UNDEFINED'"
                )
            }
        }

        val migration16To17 = object : Migration(16, 17) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `nutrition_intelligence` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `summary` TEXT NOT NULL, `evidenceLevel` TEXT NOT NULL, `reality` TEXT NOT NULL, `recommendation` TEXT NOT NULL, `source` TEXT NOT NULL, `sourceUrl` TEXT, PRIMARY KEY(`id`))"
                )
            }
        }

        val migration17To18 = object : Migration(17, 18) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `investigatedClaim` TEXT"
                )
            }
        }

        val migration18To19 = object : Migration(18, 19) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `education_lessons` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `summary` TEXT NOT NULL, `content` TEXT NOT NULL, `keyTakeaway` TEXT NOT NULL, `source` TEXT NOT NULL, `sourceUrl` TEXT, PRIMARY KEY(`id`))"
                )
            }
        }

        val migration19To20 = object : Migration(19, 20) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `prevention_actions` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `iconName` TEXT NOT NULL, `isAdopted` INTEGER NOT NULL, PRIMARY KEY(`id`))"
                )
            }
        }

        val migration20To21 = object : Migration(20, 21) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `theTruth` TEXT"
                )
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `theCommand` TEXT"
                )
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `theExecutionJson` TEXT NOT NULL DEFAULT '[]'"
                )
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `theShield` TEXT"
                )
            }
        }

        val migration21To22 = object : Migration(21, 22) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `isActionTaken` INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        val migration22To23 = object : Migration(22, 23) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE `signals` ADD COLUMN `safeAlternativesJson` TEXT NOT NULL DEFAULT '[]'"
                )
            }
        }

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
        .addMigrations(migration1To2, migration2To3, migration3To4, migration4To5, migration5To6, migration6To7, migration7To8, migration8To9, migration9To10, migration10To11, migration11To12, migration12To13, migration13To14, migration14To15, migration15To16, migration16To17, migration17To18, migration18To19, migration19To20, migration20To21, migration21To22, migration22To23)
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideSourceMaterialDao(database: AppDatabase): SourceMaterialDao {
        return database.sourceMaterialDao()
    }

    @Provides
    @Singleton
    fun provideSignalDao(database: AppDatabase): SignalDao {
        return database.signalDao()
    }

    @Provides
    @Singleton
    fun provideConflictDao(database: AppDatabase): ConflictDao {
        return database.conflictDao()
    }

    @Provides
    @Singleton
    fun provideThreadDao(database: AppDatabase): ThreadDao {
        return database.threadDao()
    }

    @Provides
    @Singleton
    fun provideConsolidatedEventDao(database: AppDatabase): ConsolidatedEventDao {
        return database.consolidatedEventDao()
    }

    @Provides
    @Singleton
    fun provideTimelineEntryDao(database: AppDatabase): TimelineEntryDao {
        return database.timelineEntryDao()
    }

    @Provides
    @Singleton
    fun provideSupersessionRelationDao(database: AppDatabase): SupersessionRelationDao {
        return database.supersessionRelationDao()
    }

    @Provides
    @Singleton
    fun provideStateTransitionDao(database: AppDatabase): StateTransitionDao {
        return database.stateTransitionDao()
    }

    @Provides
    @Singleton
    fun provideBriefingSnapshotDao(database: AppDatabase): BriefingSnapshotDao {
        return database.briefingSnapshotDao()
    }

    @Provides
    @Singleton
    fun provideReentryLifecycleDao(database: AppDatabase): ReentryLifecycleDao {
        return database.reentryLifecycleDao()
    }

    @Provides
    @Singleton
    fun provideReentryAuditDao(database: AppDatabase): ReentryAuditDao {
        return database.reentryAuditDao()
    }

    @Provides
    @Singleton
    fun provideNutritionIntelligenceDao(database: AppDatabase): NutritionIntelligenceDao {
        return database.nutritionIntelligenceDao()
    }

    @Provides
    @Singleton
    fun provideEducationLessonDao(database: AppDatabase): EducationLessonDao {
        return database.educationLessonDao()
    }

    @Provides
    @Singleton
    fun providePreventionActionDao(database: AppDatabase): PreventionActionDao {
        return database.preventionActionDao()
    }
}
