package com.reabuzz.admin

import androidx.compose.ui.graphics.Color

private val LightColorScheme = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF6750A4),  // Purple primary
    secondary = Color(0xFFFFC107), // Amber secondary
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF000000),
    error = Color(0xFFFFC401),
    onError = Color(0xFF000000),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),
    primaryVariant = Color(0xFF4527A0),
    secondaryVariant = Color(0xFFFFB300),
)

composable fun ReabuzzTheme(content: @ComposeUI -> Unit) {
    androidx.compose.material3.MaterialTheme(
        colorScheme = LightColorScheme,
        typography = androidx.compose.material3.typography2,
    ) {
        content()
    }
}
