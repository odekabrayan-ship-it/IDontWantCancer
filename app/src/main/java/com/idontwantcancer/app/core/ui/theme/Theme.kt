package com.idontwantcancer.app.core.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.idontwantcancer.app.core.ui.adaptive.AdaptiveProvider
import com.idontwantcancer.app.core.ui.adaptive.AdaptiveLayout

private val LightColorScheme = lightColorScheme(
    primary = DarkGreen,
    onPrimary = OffWhite,
    secondary = MutedGreen,
    onSecondary = OffWhite,
    tertiary = WarmAccent,
    background = OffWhite,
    surface = OffWhite,
    onBackground = DarkText,
    onSurface = DarkText
)

private val DarkColorScheme = darkColorScheme(
    primary = LightGreen,
    onPrimary = DarkBackground,
    secondary = MutedGreen,
    onSecondary = LightText,
    tertiary = WarmAccent,
    background = DarkBackground,
    surface = DarkBackground,
    onBackground = LightText,
    onSurface = LightText
)

@Composable
fun IDontWantCancerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    AdaptiveProvider {
        CompositionLocalProvider(LocalSpacing provides getSpacingFor(AdaptiveLayout.current)) {
            MaterialTheme(
                colorScheme = colorScheme,
                typography = Typography,
                shapes = Shapes,
                content = content
            )
        }
    }
}
