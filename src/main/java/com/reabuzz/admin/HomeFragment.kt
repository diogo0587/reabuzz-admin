package com.reabuzz.admin

import androidx.fragment.app.Fragment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        // Return a simple view since we're using Compose
        val context = requireContext()
        
        var userName by remember { mutableStateOf("") }
        
        return Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Home", fontWeight = FontWeight.Bold) },
                    navIcon = {
                        Button(
                            onClick = {
                                // Navigate to profile
                                findNavController().navigate(R.id.nav_profile)
                            }
                        ) {
                            Text("Profile")
                        }
                    }
                )
            }
            { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .align(Alignment.CenterStart),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = getString(R.string.welcome_message),
                        style = MaterialTheme.typography.h4
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = "Welcome to ReaBuzz Admin Panel",
                                style = MaterialTheme.typography.h6
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    // Login as admin for demo
                                    findNavController().navigate(R.id.nav_admin_dashboard)
                                }
                            ) {
                                Text("Login as Admin")
                            }
                        }
                    }
                }
            }
        )
    }
}
