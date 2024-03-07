package com.ramdani.danamon.presentation.main

import com.ramdani.danamon.core.base.BaseViewModel
import com.ramdani.danamon.core.base.NetworkHandler
import io.reactivex.Scheduler

class AdminMainVM(uiSchedulers: Scheduler, ioSchedulers: Scheduler, networkHandler: NetworkHandler) : BaseViewModel(
    uiSchedulers = uiSchedulers,
    ioScheduler = ioSchedulers,
    networkHandler = networkHandler
) {

}