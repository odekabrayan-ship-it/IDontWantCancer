package com.idontwantcancer.app.core.ui.adaptive

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.window.core.layout.WindowWidthSizeClass

/**
 * Represent the three canonical Material design layout categories.
 */
@Immutable
enum class AdaptiveLayoutType {
    Compact, Medium, Expanded
}

/**
 * Provides the current [AdaptiveLayoutType] to the composition.
 */
val LocalAdaptiveLayoutType = staticCompositionLocalOf { AdaptiveLayoutType.Compact }

/**
 * Returns the [AdaptiveLayoutType] derived from the current [WindowAdaptiveInfo].
 */
@Composable
fun calculateAdaptiveLayoutType(
    adaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
): AdaptiveLayoutType {
    return when (adaptiveInfo.windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> AdaptiveLayoutType.Compact
        WindowWidthSizeClass.MEDIUM -> AdaptiveLayoutType.Medium
        WindowWidthSizeClass.EXPANDED -> AdaptiveLayoutType.Expanded
        else -> AdaptiveLayoutType.Compact
    }
}

/**
 * A wrapper Composable that provides adaptive information to its children.
 */
@Composable
fun AdaptiveProvider(
    content: @Composable () -> Unit
) {
    val layoutType = calculateAdaptiveLayoutType()
    CompositionLocalProvider(
        LocalAdaptiveLayoutType provides layoutType,
        content = content
    )
}

/**
 * Accessor for the current [AdaptiveLayoutType].
 */
object AdaptiveLayout {
    val current: AdaptiveLayoutType
        @Composable
        @ReadOnlyComposable
        get() = LocalAdaptiveLayoutType.current
}
