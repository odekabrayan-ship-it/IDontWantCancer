package com.idontwantcancer.app.data.local

import androidx.room.TypeConverter
import com.idontwantcancer.app.domain.model.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromLifecycle(value: IntelligenceLifecycle): String = value.name

    @TypeConverter
    fun toLifecycle(value: String): IntelligenceLifecycle = IntelligenceLifecycle.valueOf(value)

    @TypeConverter
    fun fromCategory(value: SignalCategory): String = value.name

    @TypeConverter
    fun toCategory(value: String): SignalCategory = SignalCategory.valueOf(value)

    @TypeConverter
    fun fromImportance(value: SignalImportance): String = value.name

    @TypeConverter
    fun toImportance(value: String): SignalImportance = SignalImportance.valueOf(value)

    @TypeConverter
    fun fromConfidence(value: SignalConfidence): String = value.name

    @TypeConverter
    fun toConfidence(value: String): SignalConfidence = SignalConfidence.valueOf(value)

    @TypeConverter
    fun fromConflictType(value: ConflictType): String = value.name

    @TypeConverter
    fun toConflictType(value: String): ConflictType = ConflictType.valueOf(value)

    @TypeConverter
    fun fromResolutionStatus(value: ResolutionStatus): String = value.name

    @TypeConverter
    fun toResolutionStatus(value: String): ResolutionStatus = ResolutionStatus.valueOf(value)

    @TypeConverter
    fun fromSignificanceOutcome(value: SignificanceOutcome): String = value.name

    @TypeConverter
    fun toSignificanceOutcome(value: String): SignificanceOutcome = SignificanceOutcome.valueOf(value)

    @TypeConverter
    fun fromStringList(value: List<String>): String = Json.encodeToString(value)

    @TypeConverter
    fun toStringList(value: String): List<String> = Json.decodeFromString(value)

    @TypeConverter
    fun fromSourceList(value: List<SignalSource>): String = Json.encodeToString(value)

    @TypeConverter
    fun toSourceList(value: String): List<SignalSource> = Json.decodeFromString(value)

    @TypeConverter
    fun fromEvidenceFactorList(value: List<EvidenceFactor>): String = Json.encodeToString(value)

    @TypeConverter
    fun toEvidenceFactorList(value: String): List<EvidenceFactor> = Json.decodeFromString(value)

    @TypeConverter
    fun fromSupersessionType(value: SupersessionType): String = value.name

    @TypeConverter
    fun toSupersessionType(value: String): SupersessionType = SupersessionType.valueOf(value)

    @TypeConverter
    fun fromTimelineType(value: TimelineEntryType): String = value.name

    @TypeConverter
    fun toTimelineType(value: String): TimelineEntryType = TimelineEntryType.valueOf(value)

    @TypeConverter
    fun fromTransitionType(value: IntelligenceStateTransitionType): String = value.name

    @TypeConverter
    fun toTransitionType(value: String): IntelligenceStateTransitionType = IntelligenceStateTransitionType.valueOf(value)

    @TypeConverter
    fun fromBriefingStatus(value: BriefingStatus): String = value.name

    @TypeConverter
    fun toBriefingStatus(value: String): BriefingStatus = BriefingStatus.valueOf(value)

    @TypeConverter
    fun fromReentryLifecycleState(value: ReentryLifecycleState): String = value.name

    @TypeConverter
    fun toReentryLifecycleState(value: String): ReentryLifecycleState = ReentryLifecycleState.valueOf(value)
}
