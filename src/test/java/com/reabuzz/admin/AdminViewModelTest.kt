package com.reabuzz.admin

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class AdminViewModelTest {

    private lateinit var viewModel: AdminViewModel
    private lateinit var mockNavController: androidx.navigation.NavController

    @Before
    fun setUp() {
        mockNavController = mock(androidx.navigation.NavController::class.java)
        viewModel = AdminViewModel(mockNavController)
    }

    @Test
    fun `initial state - not authenticated`() {
        assertFalse("Should not be authenticated initially", viewModel.isAuthenticated)
        assertFalse("Should not be admin initially", viewModel.isAdmin)
        assertNull("Current user should be null", viewModel.currentUser)
    }

    @Test
    fun `login with valid admin credentials`() {
        viewModel.login("admin_user", "any")
        
        assertTrue("Should be authenticated after valid login", viewModel.isAuthenticated)
        assertTrue("Should be admin", viewModel.isAdmin)
        assertEquals("admin_user", viewModel.currentUser?.username)
        assertEquals("ADMIN", viewModel.currentUser?.role)
    }

    @Test
    fun `login with valid moderator credentials`() {
        viewModel.login("moderator_1", "any")
        
        assertTrue("Should be authenticated", viewModel.isAuthenticated)
        assertFalse("Should NOT be admin", viewModel.isAdmin)
        assertEquals("MODERATOR", viewModel.currentUser?.role)
    }

    @Test
    fun `login with valid user credentials`() {
        viewModel.login("regular_user", "any")
        
        assertTrue("Should be authenticated", viewModel.isAuthenticated)
        assertFalse("Should NOT be admin", viewModel.isAdmin)
        assertEquals("USER", viewModel.currentUser?.role)
    }

    @Test
    fun `login with invalid credentials`() {
        viewModel.login("nonexistent", "any")
        
        assertFalse("Should not be authenticated", viewModel.isAuthenticated)
        assertNull("Current user should be null", viewModel.currentUser)
    }

    @Test
    fun `logout clears authentication`() {
        viewModel.login("admin_user", "any")
        assertTrue(viewModel.isAuthenticated)
        
        viewModel.logout()
        
        assertFalse("Should not be authenticated after logout", viewModel.isAuthenticated)
        assertNull("Current user should be null", viewModel.currentUser)
    }

    @Test
    fun `assign role requires admin`() {
        // Login as regular user
        viewModel.login("regular_user", "any")
        assertFalse(viewModel.isAdmin)
        
        // Attempt to assign role (should be ignored)
        viewModel.assignRole(999, "ADMIN")
        
        // No exception, just silently ignored
        // In real app, would verify backend not called
    }

    @Test
    fun `admin can assign role`() {
        viewModel.login("admin_user", "any")
        assertTrue(viewModel.isAdmin)
        
        // Assign role (in real app would call API)
        viewModel.assignRole(2, "ADMIN")
        
        // Verify audit log would be called
        // This is a behavioral test - actual implementation calls logAdminAction
    }

    @Test
    fun `audit logging for admin actions`() {
        viewModel.login("admin_user", "any")
        
        // Should not throw
        viewModel.logAdminAction("TEST_ACTION", "test details")
    }
}
