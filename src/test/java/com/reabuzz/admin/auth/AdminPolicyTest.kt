package com.reabuzz.admin.auth

import com.reabuzz.admin.model.Account
import com.reabuzz.admin.model.Permission
import com.reabuzz.admin.model.Role
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdminPolicyTest {

    private val admin = Account(1, "admin", setOf(Role.USER, Role.ADMIN))
    private val vip = Account(2, "vip", setOf(Role.USER, Role.VIP))
    private val adminVip = Account(3, "owner", setOf(Role.USER, Role.ADMIN, Role.VIP))
    private val user = Account(4, "user", setOf(Role.USER))

    @Test
    fun `admin gets the administrative permissions but not the vip panel`() {
        val granted = AdminPolicy.permissionsFor(admin)
        assertTrue(
            granted.containsAll(
                listOf(
                    Permission.VIEW_ADMIN_PANEL,
                    Permission.MANAGE_USERS,
                    Permission.ASSIGN_ROLES,
                    Permission.VIEW_AUDIT_LOG,
                    Permission.SYSTEM_CONFIG,
                )
            )
        )
        assertFalse(granted.contains(Permission.VIP_PANEL))
    }

    @Test
    fun `vip gets only the vip panel`() {
        assertEquals(setOf(Permission.VIP_PANEL), AdminPolicy.permissionsFor(vip))
    }

    @Test
    fun `admin with vip gets both sets`() {
        val granted = AdminPolicy.permissionsFor(adminVip)
        assertTrue(granted.contains(Permission.VIEW_ADMIN_PANEL))
        assertTrue(granted.contains(Permission.VIP_PANEL))
    }

    @Test
    fun `plain user gets nothing`() {
        assertEquals(emptySet<Permission>(), AdminPolicy.permissionsFor(user))
    }

    @Test
    fun `anonymous is denied everything`() {
        Permission.entries.forEach { permission ->
            assertFalse(AdminPolicy.can(null, permission, debugToolsEnabled = true))
        }
    }

    @Test
    fun `debug tools need both the admin role and a debug build`() {
        assertTrue(AdminPolicy.can(adminVip, Permission.DEBUG_TOOLS, debugToolsEnabled = true))
        assertFalse(
            "release build must not grant debug tools",
            AdminPolicy.can(adminVip, Permission.DEBUG_TOOLS, debugToolsEnabled = false),
        )
        assertFalse("vip alone is not enough", AdminPolicy.can(vip, Permission.DEBUG_TOOLS, true))
        assertFalse(AdminPolicy.can(user, Permission.DEBUG_TOOLS, true))
    }
}
