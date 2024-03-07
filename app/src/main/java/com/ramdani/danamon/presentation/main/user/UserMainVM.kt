package com.ramdani.danamon.presentation.main.user

import androidx.paging.PagingData
import com.ramdani.danamon.core.base.BaseViewModel
import com.ramdani.danamon.core.base.NetworkHandler
import com.ramdani.danamon.core.extenstions.disposedBy
import com.ramdani.danamon.core.extenstions.generalErrorServer
import com.ramdani.danamon.core.utils.SingleLiveEvent
import com.ramdani.danamon.data.repository.PhotoRepository
import com.ramdani.danamon.data.response.ResponseDataPhoto
import io.reactivex.Scheduler

class UserMainVM(uiScheduler: Scheduler, ioScheduler: Scheduler, networkHandler: NetworkHandler,private val repository: PhotoRepository) : BaseViewModel(
    uiSchedulers = uiScheduler,
    ioScheduler = ioScheduler,
    networkHandler = networkHandler,
) {

    val photoEvent = SingleLiveEvent<PagingData<ResponseDataPhoto>>()
    val progressBarEvent = SingleLiveEvent<Boolean>()
    fun getPhoto(){
        executeJob {
            repository.getDataPhoto()
                .compose(applyFlowableSchedulers())
                .doOnSubscribe { progressBarEvent.postValue(true) }
                .doFinally { progressBarEvent.postValue(false) }
                .subscribe({
                    progressBarEvent.postValue(false)
                    photoEvent.value = it
                },{
                    progressBarEvent.postValue(false)
                    handleFailure(it.generalErrorServer())
                }).disposedBy(disposable)
        }
    }
}