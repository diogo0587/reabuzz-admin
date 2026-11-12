package com.reabuzz.admin.auth

import com.reabuzz.admin.model.Account
import com.reabuzz.admin.model.Permission

/**
 * Single source of truth for what a role set is allowed to do.
 *
 * This is a client-side projection used to render the UI. A real deployment must
 * repeat these checks on the server, which is the only place where the decision
 * is authoritative: see [AuthorizationService].
 */
object AdminPolicy {

    fun permissionsFor(account: Account): Set<Permission> {
        val granted = mutableSetOf<Permission>()
        if (account.isAdmin) {
            granted += setOf(
                Permission.VIEW_ADMIN_PANEL,
                Permission.MANAGE_USERS,
                Permission.ASSIGN_ROLES,
                Permission.VIEW_AUDIT_LOG,
                Permission.SYSTEM_CONFIG,
            )
        }
        if (account.isVip) {
            granted += Permission.VIP_PANEL
        }
        return granted
    }

    /**
     * [debugToolsEnabled] is wired to BuildConfig.DEBUG_TOOLS_ENABLED, so a release
     * build cannot grant [Permission.DEBUG_TOOLS] even for an administrator.
     */
    fun can(account: Account?, permission: Permission, debugToolsEnabled: Boolean): Boolean {
        if (account == null) return false
        if (permission == Permission.DEBUG_TOOLS) {
            return debugToolsEnabled && account.isAdmin
        }
        return permission in permissionsFor(account)
    }
}
