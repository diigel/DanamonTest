package com.ramdani.danamon.presentation.auth.register

import com.ramdani.danamon.core.base.BaseViewModel
import com.ramdani.danamon.core.base.NetworkHandler
import com.ramdani.danamon.core.enums.UserRole
import com.ramdani.danamon.core.extenstions.disposedBy
import com.ramdani.danamon.core.extenstions.generalErrorServer
import com.ramdani.danamon.core.utils.SingleLiveEvent
import com.ramdani.danamon.data.local.entity.UserEntity
import com.ramdani.danamon.data.repository.UserRepository
import io.reactivex.Scheduler

class RegisterVM(
    uiScheduler: Scheduler,
    ioScheduler: Scheduler,
    networkHandler: NetworkHandler,
    private val userRepository: UserRepository
) : BaseViewModel(uiScheduler, ioScheduler, networkHandler) {

    val createAccount = SingleLiveEvent<Pair<Boolean,String>>()

    val usernameEvent = SingleLiveEvent<String>()
    val emailEvent = SingleLiveEvent<String>()
    val passwordEvent = SingleLiveEvent<String>()
    val spinnerEvent = SingleLiveEvent<UserRole>()

    fun createAccount(userEntity: UserEntity) {
        executeJob {
            userRepository.createAccount(userEntity)
                .compose(applyCompletableSchedulers())
                .doOnSubscribe { isLoadingLiveData.postValue(true) }
                .doOnTerminate { isLoadingLiveData.postValue(false) }
                .subscribe({
                    createAccount.value = Pair(true,"Create Account Success")
                },{
                    createAccount.value = Pair(false,it.message ?: "Create Account Failed!")
                }).disposedBy(disposable)
        }
    }
}