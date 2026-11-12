package com.reabuzz.admin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.reabuzz.admin.AdminViewModel
import com.reabuzz.admin.DebugTools
import com.reabuzz.admin.audit.AuditEntry
import com.reabuzz.admin.model.Permission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(title: String, content: @Composable () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(title, fontWeight = FontWeight.Bold) }) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            content()
        }
    }
}

@Composable
fun LoginScreen(viewModel: AdminViewModel, onAuthenticated: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    ScreenScaffold("ReaBuzz Admin") {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("login_username"),
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("login_password"),
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.login(username, password)
                if (viewModel.isAuthenticated) onAuthenticated()
            },
            modifier = Modifier.testTag("login_button"),
        ) {
            Text("Sign in")
        }
        val state = viewModel.loginState
        if (state is AdminViewModel.LoginState.Failed) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Sign-in failed (${state.error})",
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Composable
fun HomeScreen(viewModel: AdminViewModel) {
    ScreenScaffold("Home") {
        Text("Welcome to the ReaBuzz admin console.", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Signed in as", style = MaterialTheme.typography.labelMedium)
                Text(
                    viewModel.account?.username ?: "not signed in",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.testTag("home_username"),
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(viewModel: AdminViewModel) {
    ScreenScaffold("Profile") {
        val account = viewModel.account
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Username: ${account?.username ?: "-"}")
                Spacer(Modifier.height(4.dp))
                Text("Roles: ${account?.roles?.joinToString() ?: "-"}")
            }
        }
    }
}

@Composable
fun AdminDashboardScreen(viewModel: AdminViewModel, auditEntries: List<AuditEntry>) {
    ScreenScaffold("Admin Dashboard") {
        if (!viewModel.can(Permission.VIEW_ADMIN_PANEL)) {
            Text("Administrative functions are restricted for this account.")
            return@ScreenScaffold
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Administrator panel", fontWeight = FontWeight.Bold)
                        Text("All actions are recorded in the audit log.")
                    }
                }
            }
            if (viewModel.can(Permission.VIP_PANEL)) {
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text("VIP panel", fontWeight = FontWeight.Bold)
                            Text("Exclusive to accounts holding the VIP role.")
                        }
                    }
                }
            }
            DebugTools.content?.let { debugContent ->
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) { debugContent() }
                    }
                }
            }
            item {
                Text("Audit log", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
            }
            if (auditEntries.isEmpty()) {
                item { Text("No entries yet.") }
            } else {
                items(auditEntries) { entry ->
                    Text("${entry.actorUsername} ${entry.action} ${entry.details ?: ""} [${entry.outcome}]")
                }
            }
        }
    }
}
