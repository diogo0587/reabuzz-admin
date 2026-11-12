package com.reabuzz.admin

import androidx.compose.runtime.Composable

/**
 * Slot the debug variant fills with extra admin UI. It stays null in release
 * builds, so no debug-only symbol is ever referenced from main.
 */
object DebugTools {
    var content: (@Composable () -> Unit)? = null
}
