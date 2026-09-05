package com.idontwantcancer.app.domain.model

/**
 * Defines major nutritional staple groups that vary by region.
 */
enum class StapleGroup {
    FIBER_STAPLE,
    PROTEIN_STAPLE,
    HEART_HEALTHY_OIL
}

/**
 * Registry mapping global regions to specific staple food strings.
 */
object RegionalStapleRegistry {
    private val regionMap = mapOf(
        "US" to mapOf(
            StapleGroup.FIBER_STAPLE to "Whole Wheat, Oats, Brown Rice",
            StapleGroup.PROTEIN_STAPLE to "Beans, Lentils, Poultry",
            StapleGroup.HEART_HEALTHY_OIL to "Olive Oil, Canola Oil"
        ),
        "KE" to mapOf(
            StapleGroup.FIBER_STAPLE to "Sorghum, Teff, Millet, Brown Maize",
            StapleGroup.PROTEIN_STAPLE to "Lentils, Beans, Fresh Fish",
            StapleGroup.HEART_HEALTHY_OIL to "Sunflower Oil"
        ),
        "GB" to mapOf(
            StapleGroup.FIBER_STAPLE to "Whole Wheat, Barley, Oats",
            StapleGroup.PROTEIN_STAPLE to "Pulses, Peas, Lean Poultry",
            StapleGroup.HEART_HEALTHY_OIL to "Rapeseed Oil, Olive Oil"
        ),
        "GLOBAL" to mapOf(
            StapleGroup.FIBER_STAPLE to "Whole Grains, Beans, Lentils",
            StapleGroup.PROTEIN_STAPLE to "Legumes, Fresh Fish, Poultry",
            StapleGroup.HEART_HEALTHY_OIL to "Unsaturated Vegetable Oils"
        )
    )

    fun getStaple(group: StapleGroup, countryCode: String?): String {
        val mapping = regionMap[countryCode?.uppercase()] ?: regionMap["GLOBAL"]!!
        return mapping[group] ?: regionMap["GLOBAL"]!![group]!!
    }
}
