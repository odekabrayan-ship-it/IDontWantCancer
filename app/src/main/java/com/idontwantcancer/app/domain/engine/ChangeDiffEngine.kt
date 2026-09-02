package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.DetectedChange
import com.idontwantcancer.app.domain.model.IntelligenceThread
import com.idontwantcancer.app.domain.model.Signal
import com.idontwantcancer.app.domain.model.SourceMaterial

/**
 * Interface for the intelligence component responsible for identifying
 * meaningful changes by comparing current information with the agency's memory.
 */
interface ChangeDiffEngine {
    /**
     * Compares newly acquired material with historical context to identify
     * meaningful intelligence changes.
     *
     * @param current The newly acquired material.
     * @param previousMaterial The previously processed version of this specific material, if any.
     * @param relatedThread The existing intelligence thread this material belongs to, if any.
     * @param lastSignal The most recent signal produced for this thread, if any.
     * @return A list of detected meaningful changes.
     */
    suspend fun detectChanges(
        current: SourceMaterial,
        previousMaterial: SourceMaterial?,
        relatedThread: IntelligenceThread?,
        lastSignal: Signal?
    ): List<DetectedChange>
}
