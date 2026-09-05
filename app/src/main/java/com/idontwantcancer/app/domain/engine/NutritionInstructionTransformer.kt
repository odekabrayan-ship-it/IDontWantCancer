package com.idontwantcancer.app.domain.engine

import com.idontwantcancer.app.domain.model.RegionalStapleRegistry
import com.idontwantcancer.app.domain.model.StapleGroup
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Responsible for transforming general nutritional instructions into 
 * regionally-relevant directives using placeholders.
 */
@Singleton
class NutritionInstructionTransformer @Inject constructor() {

    /**
     * Replaces placeholders like {FIBER_STAPLE} with specific regional foods.
     */
    fun transform(instructions: List<String>, countryCode: String?): List<String> {
        return instructions.map { instruction ->
            var transformed = instruction
            StapleGroup.entries.forEach { group ->
                val placeholder = "{${group.name}}"
                if (transformed.contains(placeholder)) {
                    transformed = transformed.replace(placeholder, RegionalStapleRegistry.getStaple(group, countryCode))
                }
            }
            transformed
        }
    }
}
