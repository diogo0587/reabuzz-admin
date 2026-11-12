package com.reabuzz.admin.debug

import com.reabuzz.admin.auth.AdminPolicy
import com.reabuzz.admin.auth.AuthResult
import com.reabuzz.admin.auth.AuthorizationService
import com.reabuzz.admin.model.Account
import com.reabuzz.admin.model.Permission
import com.reabuzz.admin.model.Role

/**
 * Debug-build-only sign-in shortcut: the account [debugUsername] authenticates
 * with any password. It is compiled only into the debug variant and delegates
 * every permission decision to [AdminPolicy], so it grants nothing that the
 * release policy would not already allow for an ADMIN + VIP account.
 */
class DebugAuthorizationService(
    private val delegate: AuthorizationService,
    private val debugUsername: String = "debug_admin",
) : AuthorizationService {

    override fun authenticate(username: String, password: String): AuthResult =
        if (username == debugUsername) {
            AuthResult(
                Account(
                    id = 0L,
                    username = debugUsername,
                    roles = setOf(Role.USER, Role.ADMIN, Role.VIP),
                )
            )
        } else {
            delegate.authenticate(username, password)
        }

    override fun can(account: Account?, permission: Permission): Boolean =
        delegate.can(account, permission)
}
