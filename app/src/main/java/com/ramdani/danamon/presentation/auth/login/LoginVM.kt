package com.ramdani.danamon.presentation.auth.login

import androidx.room.EmptyResultSetException
import com.ramdani.danamon.core.base.BaseViewModel
import com.ramdani.danamon.core.base.NetworkHandler
import com.ramdani.danamon.core.extenstions.disposedBy
import com.ramdani.danamon.core.extenstions.generalErrorServer
import com.ramdani.danamon.core.utils.Failure
import com.ramdani.danamon.core.utils.SingleLiveEvent
import com.ramdani.danamon.data.local.entity.UserEntity
import com.ramdani.danamon.data.repository.UserRepository
import io.reactivex.Scheduler

class LoginVM(
    uiScheduler: Scheduler,
    ioScheduler: Scheduler,
    networkHandler: NetworkHandler,
    private val userRepository: UserRepository
) : BaseViewModel(uiScheduler, ioScheduler, networkHandler) {

    val getAccount = SingleLiveEvent<UserEntity>()

    fun getAccount(userName : String,password : String) {
        executeJob {
            userRepository.getAccount(userName,password)
                .compose(applySchedulers())
                .doOnSubscribe { isLoadingLiveData.postValue(true) }
                .doOnTerminate { isLoadingLiveData.postValue(false) }
                .subscribe({ user ->
                    getAccount.value = user
                },{
                    if (it is EmptyResultSetException)handleFailure(Failure.DataNotExist("User Not Found")) else handleFailure(it.generalErrorServer())
                }).disposedBy(disposable)

        }
    }
}