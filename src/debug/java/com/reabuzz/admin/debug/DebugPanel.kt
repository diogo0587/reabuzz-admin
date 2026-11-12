package com.reabuzz.admin.debug

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Debug-build-only panel, rendered only when DebugTools.content is populated. */
@Composable
fun DebugPanel() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Debug tools")
            Text("Sign in as \"debug_admin\" (any password) to act as ADMIN + VIP.")
        }
    }
}
