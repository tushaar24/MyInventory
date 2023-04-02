package com.example.myinventory

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class MyInventoryApplication : Application() {
    companion object {
        @JvmStatic
        val AppScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    }
}