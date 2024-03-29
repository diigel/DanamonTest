package com.ramdani.danamon.presentation.main.user

import androidx.paging.PagingData
import com.ramdani.danamon.core.base.BaseViewModel
import com.ramdani.danamon.core.base.NetworkHandler
import com.ramdani.danamon.core.extenstions.disposedBy
import com.ramdani.danamon.core.extenstions.generalErrorServer
import com.ramdani.danamon.core.utils.SingleLiveEvent
import com.ramdani.danamon.data.repository.AccountRepository
import com.ramdani.danamon.data.repository.PhotoRepository
import com.ramdani.danamon.data.response.ResponseDataPhoto
import io.reactivex.Scheduler

class UserMainVM(
    uiScheduler: Scheduler, ioScheduler: Scheduler, networkHandler: NetworkHandler,
    private val repository: PhotoRepository,
    private val accountRepository: AccountRepository
) : BaseViewModel(
    uiSchedulers = uiScheduler,
    ioScheduler = ioScheduler,
    networkHandler = networkHandler,
) {

    val photoEvent = SingleLiveEvent<PagingData<ResponseDataPhoto>>()
    val progressBarEvent = SingleLiveEvent<Boolean>()
    val updateActiveAccountEvent = SingleLiveEvent<Boolean>()

    fun getPhoto() {
        executeJob {
            repository.getDataPhoto()
                .compose(applyFlowableSchedulers())
                .doOnSubscribe { progressBarEvent.postValue(true) }
                .doFinally { progressBarEvent.postValue(false) }
                .subscribe({
                    progressBarEvent.postValue(false)
                    photoEvent.value = it
                }, {
                    progressBarEvent.postValue(false)
                    handleFailure(it.generalErrorServer())
                }).disposedBy(disposable)
        }
    }

    fun updateActiveAccount(userId: Int, isActive: Boolean) {
        executeJob {
            accountRepository.updateActiveAccount(userId, isActive)
                .compose(applyCompletableSchedulers())
                .doOnSubscribe { isLoadingLiveData.postValue(true) }
                .doOnTerminate { isLoadingLiveData.postValue(false) }
                .subscribe({
                    updateActiveAccountEvent.value = true
                }, {
                    updateActiveAccountEvent.value = false
                    handleFailure(it.generalErrorServer())
                }).disposedBy(disposable)
        }
    }
}