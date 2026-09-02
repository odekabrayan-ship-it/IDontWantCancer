package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

/**
 * Default implementation of [ChangeDiffEngine] that uses deterministic rules
 * to identify meaningful changes across the temporal intelligence lifecycle.
 */
class DefaultChangeDiffEngine @Inject constructor() : ChangeDiffEngine {

    override suspend fun detectChanges(
        current: SourceMaterial,
        previousMaterial: SourceMaterial?,
        relatedThread: IntelligenceThread?,
        lastSignal: Signal?
    ): List<DetectedChange> {
        val changes = mutableListOf<DetectedChange>()
        val now = Instant.now()

        // 1. Check for Identical Content (Memory lookup for this specific material)
        if (previousMaterial != null && current.contentHash == previousMaterial.contentHash) {
            return emptyList()
        }

        // 2. Identify Change Nature
        // If we have a related thread, we are comparing against the known state of that topic.
        val changeType = when {
            relatedThread == null -> ChangeType.NEW_INFORMATION
            
            // Reversal Detection
            current.content.contains("reverses", ignoreCase = true) || 
            current.content.contains("no longer recommended", ignoreCase = true) -> ChangeType.REVERSAL

            // Correction Detection
            current.content.contains("correction", ignoreCase = true) || 
            current.content.contains("erratum", ignoreCase = true) -> ChangeType.CORRECTION
            
            // Recommendation Change
            lastSignal?.recommendedAction != null && 
            current.content.contains("new recommendation", ignoreCase = true) -> ChangeType.RECOMMENDATION_CHANGE
            
            // Default to Material Change if we have a thread but no previous version of this specific material
            else -> ChangeType.MATERIAL_CHANGE
        }

        // 3. Create Detected Change
        changes.add(
            DetectedChange(
                id = UUID.randomUUID().toString(),
                type = changeType,
                sourceId = current.sourceId,
                contentId = current.contentId,
                threadId = relatedThread?.id,
                description = "Change detected in ${current.title}: ${current.content.take(100)}...",
                detectedAt = now,
                previousStateReference = previousMaterial?.contentHash,
                currentStateReference = current.contentHash
            )
        )

        return changes
    }
}
