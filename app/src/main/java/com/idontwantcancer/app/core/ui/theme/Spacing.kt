package com.idontwantcancer.app.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Spacing(
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val extraLarge: Dp = 32.dp,
    val screenPadding: Dp = 16.dp,
    val cardPadding: Dp = 16.dp,
    val sectionSpacing: Dp = 24.dp
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }

/**
 * Returns [Spacing] tailored to the provided [AdaptiveLayoutType].
 */
fun getSpacingFor(layoutType: com.idontwantcancer.app.core.ui.adaptive.AdaptiveLayoutType): Spacing {
    return when (layoutType) {
        com.idontwantcancer.app.core.ui.adaptive.AdaptiveLayoutType.Compact -> Spacing(
            small = 8.dp,
            medium = 16.dp,
            large = 24.dp,
            screenPadding = 16.dp,
            cardPadding = 16.dp,
            sectionSpacing = 24.dp
        )
        com.idontwantcancer.app.core.ui.adaptive.AdaptiveLayoutType.Medium -> Spacing(
            small = 10.dp,
            medium = 20.dp,
            large = 32.dp,
            screenPadding = 24.dp,
            cardPadding = 20.dp,
            sectionSpacing = 32.dp
        )
        com.idontwantcancer.app.core.ui.adaptive.AdaptiveLayoutType.Expanded -> Spacing(
            small = 12.dp,
            medium = 24.dp,
            large = 48.dp,
            screenPadding = 32.dp,
            cardPadding = 24.dp,
            sectionSpacing = 48.dp
        )
    }
}
