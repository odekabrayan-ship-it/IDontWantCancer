package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.*

/**
 * Interface for the intelligence component responsible for determining whether
 * a detected change is significant enough to continue through the pipeline.
 */
interface IntelligenceSignificanceGate {
    /**
     * Evaluates the significance of a detected change.
     *
     * @param change The detected change to evaluate.
     * @param assessment The evidence assessment for the change.
     * @param source The source that provided the intelligence.
     * @return The significance decision.
     */
    fun evaluate(
        change: DetectedChange,
        assessment: EvidenceAssessment,
        source: IntelligenceSource
    ): SignificanceDecision
}
