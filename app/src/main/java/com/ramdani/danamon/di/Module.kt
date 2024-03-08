package com.ramdani.danamon.di

import android.app.Application
import androidx.room.Room
import com.ramdani.danamon.core.base.NetworkHandler
import com.ramdani.danamon.core.base.SharedPreferencesHelper
import com.ramdani.danamon.data.Service
import com.ramdani.danamon.data.local.database.DatabaseConfig
import com.ramdani.danamon.data.local.dao.AccountDao
import com.ramdani.danamon.data.repository.PhotoRepository
import com.ramdani.danamon.data.repository.AccountRepository
import com.ramdani.danamon.data.repositoryImpl.PhotoRepositoryImpl
import com.ramdani.danamon.data.repositoryImpl.AccountRepositoryImpl
import com.ramdani.danamon.presentation.auth.login.LoginVM
import com.ramdani.danamon.presentation.auth.register.RegisterVM
import com.ramdani.danamon.presentation.main.admin.AdminMainVM
import com.ramdani.danamon.presentation.main.user.UserMainVM
import com.ramdani.danamon.presentation.splash.SplashVM
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

const val uiSchedulers = "uiScheduler"
const val ioSchedulers = "ioScheduler"

val common = module {
    single(qualifier(ioSchedulers)) { Schedulers.io() }
    single<Scheduler>(qualifier(uiSchedulers)) { AndroidSchedulers.mainThread() }
    single { NetworkHandler(androidContext()) }
    single { SharedPreferencesHelper(get()) }
}

val service = module {
    single {
        Service.createService()
    }
}

val database = module {
    fun provideDatabase(application: Application): DatabaseConfig {
        return Room.databaseBuilder(
            application,
            DatabaseConfig::class.java, "user_database.db"
        ).fallbackToDestructiveMigration().build()
    }

    fun provideFavoriteDao(database: DatabaseConfig): AccountDao {
        return database.userDao()
    }
    single {
        provideDatabase(androidApplication())
    }

    single { provideFavoriteDao(get()) }
}

val accountRepository = module {
    single<AccountRepository> { AccountRepositoryImpl(get()) }
}

val login = module {
    viewModel {
        LoginVM(
            uiScheduler = get(qualifier(uiSchedulers)),
            ioScheduler = get(qualifier(ioSchedulers)),
            networkHandler = get(),
            accountRepository = get(),
            sharedPreferencesHelper = get()
        )
    }
}

val register = module {
    viewModel {
        RegisterVM(
            uiScheduler = get(qualifier(uiSchedulers)),
            ioScheduler = get(qualifier(ioSchedulers)),
            networkHandler = get(),
            accountRepository = get()
        )
    }
}

val userMain = module {
    single<PhotoRepository> { PhotoRepositoryImpl(get()) }
    viewModel {
        UserMainVM(
            uiScheduler = get(qualifier(uiSchedulers)),
            ioScheduler = get(qualifier(ioSchedulers)),
            networkHandler = get(),
            repository = get(),
            accountRepository = get()
        )
    }
}

val adminMain = module {
    viewModel {
        AdminMainVM(
            uiSchedulers = get(qualifier(uiSchedulers)),
            ioSchedulers = get(qualifier(ioSchedulers)),
            networkHandler = get(),
            accountRepository = get(),
            sharedPreferencesHelper = get()
        )
    }
}

val splash = module {
    viewModel {
        SplashVM(
            uiSchedulers = get(qualifier(uiSchedulers)),
            ioSchedulers = get(qualifier(ioSchedulers)),
            networkHandler = get(),
            accountRepository = get(),
            sharedPreferencesHelper = get()
        )
    }
}