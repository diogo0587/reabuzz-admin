package com.reabuzz.admin

import androidx.fragment.app.Fragment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        var username by remember { mutableStateOf("") }
        var userRole by remember { mutableStateOf("USER") }
        
        return Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Profile", fontWeight = FontWeight.Bold) }
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
                        text = "User Profile",
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
                                text = "Username: $username",
                                style = MaterialTheme.typography.body1
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Role: $userRole",
                                style = MaterialTheme.typography.body1
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            if (userRole == "ADMIN") {
                                Button(
                                    onClick = {
                                        // Navigate to admin dashboard
                                        val args = androidx.navigation.NavArgs()
                                        args.clearTask = true
                                        // This would navigate with proper arguments
                                    }
                                ) {
                                    Text("Go to Admin Dashboard")
                                }
                            } else {
                                Text(
                                    text = "Administrative functions restricted",
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
