package com.reabuzz.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.reabuzz.admin.model.Permission
import com.reabuzz.admin.ui.AdminDashboardScreen
import com.reabuzz.admin.ui.HomeScreen
import com.reabuzz.admin.ui.LoginScreen
import com.reabuzz.admin.ui.ProfileScreen
import com.reabuzz.admin.ui.ReabuzzTheme

class MainActivity : ComponentActivity() {

    private val viewModel: AdminViewModel by viewModels {
        AdminViewModelFactory(application as ReabuzzApplication)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReabuzzTheme {
                AppRoot(viewModel)
            }
        }
    }
}

private enum class Tab(val label: String) { Home("Home"), Profile("Profile"), Admin("Admin") }

@Composable
private fun AppRoot(viewModel: AdminViewModel) {
    var tab by remember { mutableStateOf(Tab.Home) }

    if (!viewModel.isAuthenticated) {
        LoginScreen(viewModel) { tab = Tab.Home }
        return
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                Tab.entries.forEach { entry ->
                    if (entry == Tab.Admin && !viewModel.can(Permission.VIEW_ADMIN_PANEL)) return@forEach
                    NavigationBarItem(
                        selected = tab == entry,
                        onClick = { tab = entry },
                        icon = {
                            Icon(
                                when (entry) {
                                    Tab.Home -> Icons.Filled.Home
                                    Tab.Profile -> Icons.Filled.Person
                                    Tab.Admin -> Icons.Filled.AdminPanelSettings
                                },
                                contentDescription = entry.label,
                            )
                        },
                        label = { Text(entry.label) },
                    )
                }
            }
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            when (tab) {
                Tab.Home -> HomeScreen(viewModel)
                Tab.Profile -> ProfileScreen(viewModel)
                Tab.Admin -> AdminDashboardScreen(viewModel, viewModel.auditEntries())
            }
        }
    }
}
