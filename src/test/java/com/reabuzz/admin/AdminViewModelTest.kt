package com.reabuzz.admin

import com.reabuzz.admin.audit.InMemoryAuditLog
import com.reabuzz.admin.audit.Outcome
import com.reabuzz.admin.auth.DemoAuthorizationService
import com.reabuzz.admin.model.Permission
import com.reabuzz.admin.model.Role
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdminViewModelTest {

    private val log = InMemoryAuditLog()
    private val viewModel = AdminViewModel(
        auth = DemoAuthorizationService(ownerUsername = "owner", debugToolsEnabled = false),
        auditLog = log,
    )

    @Test
    fun `starts signed out and denies everything`() {
        assertFalse(viewModel.isAuthenticated)
        assertFalse(viewModel.can(Permission.VIEW_ADMIN_PANEL))
    }

    @Test
    fun `owner signs in and reaches the admin panel`() {
        viewModel.login("owner", "changeme")
        assertTrue(viewModel.isAuthenticated)
        assertEquals("owner", viewModel.account?.username)
        assertTrue(viewModel.can(Permission.VIEW_ADMIN_PANEL))
        assertTrue(viewModel.can(Permission.VIP_PANEL))
    }

    @Test
    fun `bad credentials leave the session signed out`() {
        viewModel.login("owner", "nope")
        assertFalse(viewModel.isAuthenticated)
        assertTrue(viewModel.loginState is AdminViewModel.LoginState.Failed)
    }

    @Test
    fun `logout clears the account`() {
        viewModel.login("owner", "changeme")
        viewModel.logout()
        assertFalse(viewModel.isAuthenticated)
        assertFalse(viewModel.can(Permission.VIEW_ADMIN_PANEL))
    }

    @Test
    fun `authorised role assignment is recorded as success`() {
        viewModel.login("owner", "changeme")
        assertTrue(viewModel.assignRole(targetUserId = 42L, newRole = Role.VIP))
        val entry = log.entries().single()
        assertEquals("ASSIGN_ROLE", entry.action)
        assertEquals(Outcome.SUCCESS, entry.outcome)
    }

    @Test
    fun `unauthorised role assignment is refused and audited`() {
        viewModel.login("regular_user", "changeme")
        assertFalse(viewModel.assignRole(targetUserId = 42L, newRole = Role.ADMIN))
        val entry = log.entries().single()
        assertEquals(Outcome.DENIED, entry.outcome)
        assertEquals("regular_user", entry.actorUsername)
    }

    @Test
    fun `debug tools stay off in a release-shaped policy`() {
        viewModel.login("owner", "changeme")
        assertFalse(viewModel.can(Permission.DEBUG_TOOLS))
    }
}
