package com.ramdani.danamon

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.ramdani.danamon.di.common
import com.ramdani.danamon.di.database
import com.ramdani.danamon.di.login
import com.ramdani.danamon.di.register
import com.ramdani.danamon.di.service
import com.ramdani.danamon.di.userMain
import com.ramdani.danamon.di.userRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        startKoin {
            androidContext(this@MainApplication)
            modules(
                common,
                database,
                service,
                userRepository,
                login,
                register,
                userMain
            )
        }
    }
}