package com.idontwantcancer.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class EvidenceVerdict {
    SUPPORTED,
    PARTLY_SUPPORTED,
    UNCERTAIN,
    MISLEADING,
    NOT_SUPPORTED
}
