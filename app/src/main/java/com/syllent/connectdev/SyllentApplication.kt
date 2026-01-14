package com.syllent.connectdev

import android.app.Application
import android.util.Log
import androidx.multidex.MultiDex
import com.thingclips.smart.android.common.utils.L
import com.thingclips.smart.home.sdk.ThingHomeSdk

/**
 * Application class responsible for initializing the Tuya SmartLife SDK.
 * The SDK must be initialized before any other Tuya API calls.
 */
class SyllentApplication : Application() {

    companion object {
        private const val TAG = "SyllentApplication"

        lateinit var instance: SyllentApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Initialize MultiDex
        MultiDex.install(this)

        // Initialize Tuya SmartLife SDK
        initTuyaSdk()
    }

    private fun initTuyaSdk() {
        // Initialize the SDK with debug mode enabled for development
        ThingHomeSdk.init(this)

        // Enable debug logging in development
        if (BuildConfig.DEBUG) {
            ThingHomeSdk.setDebugMode(true)
            L.setSendLogOn(true)
        }

        Log.d(TAG, "Tuya SDK initialized successfully")
    }

    override fun onTerminate() {
        super.onTerminate()
        // Clean up SDK resources
        ThingHomeSdk.onDestroy()
    }
}
