package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class DailyBriefingStatus {
    CLEAR,
    ATTENTION,
    ACTION_REQUIRED
}
