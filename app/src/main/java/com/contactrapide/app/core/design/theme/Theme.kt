package com.contactrapide.app.core.design.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
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

@Composable
fun ContactRapideTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Navy.toArgb()
            window.navigationBarColor = White.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = CrShapes,
        content = content
    )
}
