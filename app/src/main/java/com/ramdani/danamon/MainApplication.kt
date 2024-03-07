package com.ramdani.danamon

import android.app.Application
import com.ramdani.danamon.di.common
import com.ramdani.danamon.di.database
import com.ramdani.danamon.di.login
import com.ramdani.danamon.di.register
import com.ramdani.danamon.di.service
import com.ramdani.danamon.di.userRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(
                common,
                database,
                service,
                userRepository,
                login,
                register
            )
        }
    }
}