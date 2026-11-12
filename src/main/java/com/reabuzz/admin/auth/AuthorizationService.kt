package com.reabuzz.admin.auth

import com.reabuzz.admin.model.Account
import com.reabuzz.admin.model.Permission

data class AuthResult(val account: Account?, val error: AuthError? = null)

enum class AuthError { INVALID_CREDENTIALS, NETWORK }

/**
 * The seam between the app and whoever owns the role decision.
 *
 * [DemoAuthorizationService] answers locally so the sample runs without a server.
 * A production build swaps in an implementation that posts credentials to the
 * backend and returns the roles the server asserts; the UI never has to change,
 * because it only ever asks [can].
 */
interface AuthorizationService {
    fun authenticate(username: String, password: String): AuthResult
    fun can(account: Account?, permission: Permission): Boolean
}
