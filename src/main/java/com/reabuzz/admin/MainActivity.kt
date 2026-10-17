package com.reabuzz.admin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.acl.material3.bottomnavigation.BottomNavigationBar
import com.google.acl.material3.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var adminLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set content view - will be replaced by Compose
        setContent {
            ReabuzzTheme {
                // Main UI setup
                val controller = rememberNavController()
                
                // Bottom navigation
                val backStackEntry by rememberNavBackStackEntry(navController, destinationId = R.id.nav_home)
                val viewModel by viewModel { AdminViewModel(controller) }
                
                Scaffold(
                    bottomBar = {
                        BottomNavigationView(
                            items = bottomNavigationMenu(),
                            { item ->
                                navController.navigate(item.itemId) {
                                    popUpTo(navController.graph.startDestination) { inclusive true }
                                }
                            }
                        )
                    }
                ) {
                    ContainerScreen(viewModel)
                }
            }
        }
        
        // Initialize navigation
        navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController)
        
        // Check if admin already logged in (for demo with test credentials)
        adminLoggedIn = intent.getBooleanExtra("admin_logged_in", false)
        
        if (adminLoggedIn) {
            // Navigate to admin dashboard
            navController.navigate(R.id.nav_admin_dashboard)
        }
    }
}
