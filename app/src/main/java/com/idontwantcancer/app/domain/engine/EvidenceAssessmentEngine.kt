package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.DetectedChange
import com.idontwantcancer.app.domain.model.EvidenceAssessment
import com.idontwantcancer.app.domain.model.IntelligenceSource

/**
 * Interface for the intelligence component responsible for evaluating
 * the strength and characteristics of evidence behind a detected change.
 */
interface EvidenceAssessmentEngine {
    /**
     * Evaluates a detected change using the provided source context.
     *
     * @param change The change to assess.
     * @param primarySource The source that reported the change.
     * @param corroboratingSources Optional list of other sources reporting the same change.
     * @return The evaluated evidence assessment.
     */
    suspend fun assess(
        change: DetectedChange,
        primarySource: IntelligenceSource,
        corroboratingSources: List<IntelligenceSource> = emptyList()
    ): EvidenceAssessment
}
