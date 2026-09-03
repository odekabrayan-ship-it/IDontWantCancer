package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class SignalCategory {
    FOOD,
    CONSUMER_PRODUCTS,
    ENVIRONMENT,
    SCREENING,
    MEDICINE,
    RESEARCH,
    REGULATION,
    PREVENTION,
    LIFESTYLE,
    NUTRITION,
    OCCUPATIONAL
}
