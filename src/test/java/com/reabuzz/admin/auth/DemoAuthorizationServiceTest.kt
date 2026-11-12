package com.reabuzz.admin.auth

import com.reabuzz.admin.model.Permission
import com.reabuzz.admin.model.Role
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DemoAuthorizationServiceTest {

    private fun service(debugTools: Boolean = false) =
        DemoAuthorizationService(ownerUsername = "owner", debugToolsEnabled = debugTools)

    @Test
    fun `owner authenticates as admin and vip`() {
        val account = requireNotNull(service().authenticate("owner", "changeme").account)
        assertEquals("owner", account.username)
        assertTrue(account.roles.containsAll(listOf(Role.ADMIN, Role.VIP)))
    }

    @Test
    fun `wrong password is rejected`() {
        val result = service().authenticate("owner", "wrong")
        assertNull(result.account)
        assertEquals(AuthError.INVALID_CREDENTIALS, result.error)
    }

    @Test
    fun `unknown user is rejected`() {
        assertNull(service().authenticate("nobody", "changeme").account)
    }

    @Test
    fun `regular user cannot assign roles`() {
        val svc = service()
        val user = requireNotNull(svc.authenticate("regular_user", "changeme").account)
        assertFalse(svc.can(user, Permission.ASSIGN_ROLES))
    }

    @Test
    fun `owner can assign roles`() {
        val svc = service()
        val owner = requireNotNull(svc.authenticate("owner", "changeme").account)
        assertTrue(svc.can(owner, Permission.ASSIGN_ROLES))
    }

    @Test
    fun `debug tools permission follows the build flag`() {
        val owner = requireNotNull(service().authenticate("owner", "changeme").account)
        assertFalse(service(debugTools = false).can(owner, Permission.DEBUG_TOOLS))
        assertTrue(service(debugTools = true).can(owner, Permission.DEBUG_TOOLS))
    }
}
