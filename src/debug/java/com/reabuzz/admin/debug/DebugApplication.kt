package com.reabuzz.admin.debug

import com.reabuzz.admin.DebugTools
import com.reabuzz.admin.ReabuzzApplication
import com.reabuzz.admin.auth.AuthorizationService
import com.reabuzz.admin.auth.DemoAuthorizationService

/**
 * Application class for the debug variant only, selected through the manifest
 * fragment in src/debug/AndroidManifest.xml.
 */
class DebugApplication : ReabuzzApplication() {

    override fun createAuthorizationService(): AuthorizationService =
        DebugAuthorizationService(
            delegate = DemoAuthorizationService(
                ownerUsername = com.reabuzz.admin.BuildConfig.OWNER_USERNAME,
                debugToolsEnabled = true,
            )
        )

    override fun onCreate() {
        super.onCreate()
        DebugTools.content = { DebugPanel() }
    }
}
