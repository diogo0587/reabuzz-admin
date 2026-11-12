package com.reabuzz.admin.auth

import com.reabuzz.admin.model.Account
import com.reabuzz.admin.model.Permission
import com.reabuzz.admin.model.Role

/**
 * Offline stand-in for the backend. It exists so the sample can be run and tested
 * without infrastructure, and it must not be used to protect anything real: the
 * role set it hands out is compiled into the app and therefore attacker-controlled.
 *
 * @param ownerUsername the account treated as ADMIN + VIP. Supplied at build time
 *   from the `reabuzz.ownerUsername` Gradle property (see build.gradle).
 * @param debugToolsEnabled mirrors BuildConfig.DEBUG_TOOLS_ENABLED, which is false
 *   in release builds.
 * @param directory the accounts this demo knows about.
 */
class DemoAuthorizationService(
    private val ownerUsername: String,
    private val debugToolsEnabled: Boolean,
    private val directory: List<DemoAccount> = defaultDirectory(ownerUsername),
) : AuthorizationService {

    data class DemoAccount(val username: String, val password: String, val roles: Set<Role>)

    override fun authenticate(username: String, password: String): AuthResult {
        val match = directory.firstOrNull { it.username == username && it.password == password }
            ?: return AuthResult(null, AuthError.INVALID_CREDENTIALS)
        return AuthResult(
            Account(
                id = (directory.indexOf(match) + 1).toLong(),
                username = match.username,
                roles = match.roles,
            )
        )
    }

    override fun can(account: Account?, permission: Permission): Boolean =
        AdminPolicy.can(account, permission, debugToolsEnabled)

    companion object {
        fun defaultDirectory(ownerUsername: String): List<DemoAccount> = listOf(
            DemoAccount(ownerUsername, "changeme", setOf(Role.USER, Role.ADMIN, Role.VIP)),
            DemoAccount("moderator_1", "changeme", setOf(Role.USER, Role.MODERATOR)),
            DemoAccount("vip_user", "changeme", setOf(Role.USER, Role.VIP)),
            DemoAccount("regular_user", "changeme", setOf(Role.USER)),
        )
    }
}
