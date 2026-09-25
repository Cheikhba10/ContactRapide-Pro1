package com.contactrapide.app.core.design.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = Navy,
    onPrimary = White,
    secondary = Gold,
    onSecondary = NavyDark,
    tertiary = Green,
    onTertiary = White,
    background = OffWhite,
    onBackground = TextDark,
    surface = White,
    onSurface = TextDark,
    surfaceVariant = Divider,
    onSurfaceVariant = TextGray,
    error = Red,
    onError = White
)

private val DarkColors = darkColorScheme(
    primary = Gold,
    onPrimary = NavyDark,
    secondary = Gold,
    onSecondary = NavyDark,
    tertiary = Green,
    onTertiary = White,
    background = NavyDark,
    onBackground = White,
    surface = Navy,
    onSurface = White,
    surfaceVariant = NavyLight,
    onSurfaceVariant = Gold,
    error = Red,
    onError = White
)

@Composable
fun ContactRapideTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = CrShapes,
        content = content
    )
}
