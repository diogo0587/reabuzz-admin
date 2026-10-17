package com.reabuzz.admin

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.WorkManager

class ReabuzzApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize WorkManager
        WorkManager.initialize(this, androidx.work.Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build())
        
        // Lifecycle logging
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : 
            androidx.lifecycle.DefaultLifecycleObserver {
            override fun onStart(owner: androidx.lifecycle.LifecycleOwner) {
                android.util.Log.d("Reabuzz", "App moved to foreground")
            }
            override fun onStop(owner: androidx.lifecycle.LifecycleOwner) {
                android.util.Log.d("Reabuzz", "App moved to background")
            }
        })
    }
}
