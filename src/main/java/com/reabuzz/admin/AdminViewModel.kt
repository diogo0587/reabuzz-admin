package com.reabuzz.admin

import androidx.lifecycle.ViewModel
import com.reabuzz.admin.audit.AuditEntry
import com.reabuzz.admin.audit.AuditLog
import com.reabuzz.admin.audit.Outcome
import com.reabuzz.admin.auth.AuthError
import com.reabuzz.admin.auth.AuthorizationService
import com.reabuzz.admin.model.Account
import com.reabuzz.admin.model.Permission
import com.reabuzz.admin.model.Role

class AdminViewModel(
    private val auth: AuthorizationService,
    private val auditLog: AuditLog,
) : ViewModel() {

    sealed interface LoginState {
        data object Idle : LoginState
        data object Authenticated : LoginState
        data class Failed(val error: AuthError) : LoginState
    }

    var account: Account? = null
        private set

    var loginState: LoginState = LoginState.Idle
        private set

    val isAuthenticated: Boolean get() = account != null

    fun login(username: String, password: String) {
        val result = auth.authenticate(username, password)
        account = result.account
        loginState = if (result.account != null) {
            LoginState.Authenticated
        } else {
            LoginState.Failed(result.error ?: AuthError.INVALID_CREDENTIALS)
        }
    }

    fun logout() {
        account = null
        loginState = LoginState.Idle
    }

    fun can(permission: Permission): Boolean = auth.can(account, permission)

    fun auditEntries(): List<AuditEntry> = auditLog.entries().asReversed()

    /** Returns false when the caller lacks the permission; the attempt is still audited. */
    fun assignRole(targetUserId: Long, newRole: Role): Boolean {
        val actor = account
        if (!auth.can(actor, Permission.ASSIGN_ROLES)) {
            actor?.let { record(it, "ASSIGN_ROLE", "$targetUserId -> $newRole", Outcome.DENIED) }
            return false
        }
        // The backend is the authority; this demo only records the intent.
        actor?.let { record(it, "ASSIGN_ROLE", "$targetUserId -> $newRole", Outcome.SUCCESS) }
        return true
    }

    private fun record(actor: Account, action: String, details: String?, outcome: Outcome) {
        auditLog.record(
            AuditEntry(
                timestampMs = System.currentTimeMillis(),
                actorId = actor.id,
                actorUsername = actor.username,
                action = action,
                details = details,
                outcome = outcome,
            )
        )
    }
}
