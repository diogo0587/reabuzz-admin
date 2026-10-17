package com.reabuzz.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.reabuzz.admin.databinding.ActivityMainBinding
import android.content.Intent

class AdminViewModel(navController: NavController) : ViewModel() {

    // User data (test accounts)
    data class User(
        val id: Int,
        val username: String,
        val role: String,
        val isActive: Boolean = true
    )

    // Sample test users
    private val testUsers = listOf(
        User(1, "admin_user", "ADMIN"),
        User(2, "moderator_1", "MODERATOR"),
        User(3, "regular_user", "USER")
    )

    // Current logged-in user
    private var currentUser: User? = null

    // Authentication state
    val isAuthenticated: Boolean
        get() = currentUser != null

    val isAdmin: Boolean
        get() = currentUser?.role == "ADMIN"

    // Login with test credentials
    fun login(username: String, password: String?) {
        val user = testUsers.find { it.username == username }
        if (user != null) {
            currentUser = user
            // In real app, would navigate based on role
            Log.d("AdminViewModel", "User logged in: ${user.role}")
        } else {
            currentUser = null
            Log.w("AdminViewModel", "Invalid credentials: $username")
        }
    }

    // Logout
    fun logout() {
        currentUser = null
        navController.popBackStack(navController.graph.startDestination, true)
    }

    // Admin-only operations
    fun assignRole(userId: Int, newRole: String) {
        if (currentUser?.role != "ADMIN") {
            Log.w("AdminViewModel", "Non-admin attempted role assignment")
            return
        }
        // Find and update user role
        testUsers.forEach {
            if (it.id == userId) {
                it.role = newRole
                Log.i("AdminViewModel", "Role assigned: user ${it.id} is now ${newRole}")
            }
        }
    }

    // Audit logging
    fun logAdminAction(action: String, details: String?) {
        if (currentUser?.role == "ADMIN") {
            Log.i("AdminAudit", "ADMIN [${currentUser.username}]: $action - $details")
        }
    }

    // Factory for ViewModelProvider
    fun factory(factory: ViewModelProvider.Factory): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(cls: Class<T>): T = 
                if (cls.isAssignableFrom(AdminViewModel::class)) 
                    AdminViewModel(navController) as T
                else throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
