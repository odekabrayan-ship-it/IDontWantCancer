package com.idontwantcancer.app.domain.model

import java.time.Instant

data class DailyBriefing(
    val generatedAt: Instant,
    val status: DailyBriefingStatus,
    val headline: String,
    val summary: String,
    val importantSignals: List<Signal>,
    val actionItems: List<BriefingAction>
)
