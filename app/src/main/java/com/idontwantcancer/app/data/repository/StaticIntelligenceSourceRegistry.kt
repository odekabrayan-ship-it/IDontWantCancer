package com.idontwantcancer.app.data.repository

import com.idontwantcancer.app.domain.model.*
import com.idontwantcancer.app.domain.repository.IntelligenceSourceRegistry
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A static implementation of the [IntelligenceSourceRegistry] containing
 * the initial configuration of authoritative sources.
 */
@Singleton
class StaticIntelligenceSourceRegistry @Inject constructor() : IntelligenceSourceRegistry {

    private val sources = listOf(
        IntelligenceSource(
            id = "who_iarc",
            name = "IARC - World Health Organization",
            type = IntelligenceSourceType.PUBLIC_HEALTH,
            authority = SourceAuthority.INTERNATIONAL,
            reliability = SourceReliability(
                trustLevel = SourceTrustLevel.VERY_HIGH,
                isTransparent = true,
                updateReliability = SourceTrustLevel.HIGH
            ),
            scope = GeographicScope.GLOBAL
        ),
        IntelligenceSource(
            id = "fda_food",
            name = "U.S. FDA - Food Safety",
            type = IntelligenceSourceType.FOOD_SAFETY,
            authority = SourceAuthority.NATIONAL,
            reliability = SourceReliability(
                trustLevel = SourceTrustLevel.HIGH,
                isTransparent = true,
                updateReliability = SourceTrustLevel.VERY_HIGH,
                specialization = SignalCategory.FOOD
            ),
            scope = GeographicScope.NATIONAL,
            countryCode = "US"
        ),
        IntelligenceSource(
            id = "eu_efsa",
            name = "European Food Safety Authority",
            type = IntelligenceSourceType.FOOD_SAFETY,
            authority = SourceAuthority.REGULATORY_BODY,
            reliability = SourceReliability(
                trustLevel = SourceTrustLevel.HIGH,
                isTransparent = true,
                updateReliability = SourceTrustLevel.HIGH,
                specialization = SignalCategory.FOOD
            ),
            scope = GeographicScope.REGIONAL,
            countryCode = "EU"
        ),
        IntelligenceSource(
            id = "nih_nci",
            name = "National Cancer Institute",
            type = IntelligenceSourceType.CANCER_ORGANIZATION,
            authority = SourceAuthority.NATIONAL,
            reliability = SourceReliability(
                trustLevel = SourceTrustLevel.VERY_HIGH,
                isTransparent = true,
                updateReliability = SourceTrustLevel.MODERATE
            ),
            scope = GeographicScope.GLOBAL
        )
    )

    override fun getSources(): List<IntelligenceSource> = sources

    override fun getEnabledSources(): List<IntelligenceSource> = sources.filter { it.isEnabled }
}
