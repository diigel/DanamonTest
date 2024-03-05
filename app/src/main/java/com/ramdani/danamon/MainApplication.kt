package com.ramdani.danamon

import android.app.Application
import com.ramdani.danamon.di.common
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidContext(this@MainApplication)

            modules(
                common
            )
        }
    }
}