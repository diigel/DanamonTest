package com.ramdani.danamon.di

import com.ramdani.danamon.data.Service
import com.ramdani.danamon.utils.NetworkHandler
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

const val uiSchedulers = "uiScheduler"
const val ioSchedulers = "ioScheduler"

val common = module {
    single(qualifier(ioSchedulers)) { Schedulers.io() }
    single<Scheduler>(qualifier(uiSchedulers)) { AndroidSchedulers.mainThread() }
    single { NetworkHandler(get()) }
}

val service = module {
    single {
        Service.createService()
    }
}