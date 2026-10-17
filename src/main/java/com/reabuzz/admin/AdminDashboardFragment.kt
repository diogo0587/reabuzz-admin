package com.reabuzz.admin

import androidx.fragment.app.Fragment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController

class AdminDashboardFragment : Fragment() {

    // In a real app, would use ViewModel with livedata
    private val adminActions = mutableListOf(
        "Assign Role",
        "Revoke Role", 
        "View Audit Log",
        "Manage Users",
        "System Settings",
        "VIP Admin Panel"
    )

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        var selectedAction by remember { mutableStateOf("") }
        
        return Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Admin Dashboard", fontWeight = FontWeight.Bold) },
                    actions = {
                        Button(
                            onClick = {
                                // Logout admin
                                requireActivity().resultCode = androidx.activity.result.ActivityResultContracts.StartActivityForResult().requestCode
                            }
                        ) {
                            Text("Logout", style = MaterialTheme.typography.body1)
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
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Administrator Panel",
                        style = MaterialTheme.typography.h2
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Admin action list
                    adminActions.forEach { action ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = action,
                                    style = MaterialTheme.typography.body1
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                if (action == "Assign Role") {
                                    Button(
                                        onClick = {
                                            // Navigate to role assignment
                                            // In real app, would show a dialog
                                            Log.d("AdminDashboard", "Assign Role clicked")
                                        }
                                    ) {
                                        Text("Assign Role")
                                    }
                                } else if (action == "View Audit Log") {
                                    Button(
                                        onClick = {
                                            // Show audit log
                                            Log.d("AdminDashboard", "View Audit Log clicked")
                                        }
                                    ) {
                                        Text("View Audit Log")
                                    }
                                } else if (action == "VIP Admin Panel") {
                                    Button(
                                        onClick = {
                                            // VIP admin exclusive features
                                            Log.d("AdminDashboard", "VIP Admin Panel accessed")
                                        }
                                    ) {
                                        Text("Open VIP Panel")
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Security Notice",
                                style = MaterialTheme.typography.body1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = """
                                WARNING: Administrative functions 
                                must be validated on backend. 
                                Never rely on local flags for 
                                privilege escalation.
                                """.trimIndent(),
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
            }
        )
    }
}
