package com.reabuzz.admin

import android.app.Application
import com.reabuzz.admin.audit.InMemoryAuditLog
import com.reabuzz.admin.auth.AuthorizationService
import com.reabuzz.admin.auth.DemoAuthorizationService

open class ReabuzzApplication : Application() {

    lateinit var auditLog: InMemoryAuditLog
        private set

    lateinit var authorizationService: AuthorizationService
        private set

    override fun onCreate() {
        super.onCreate()
        auditLog = InMemoryAuditLog()
        authorizationService = createAuthorizationService()
    }

    /** Overridden by the debug variant; release always gets the demo service. */
    protected open fun createAuthorizationService(): AuthorizationService =
        DemoAuthorizationService(
            ownerUsername = BuildConfig.OWNER_USERNAME,
            debugToolsEnabled = BuildConfig.DEBUG_TOOLS_ENABLED,
        )
}
