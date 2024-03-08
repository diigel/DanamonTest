package com.ramdani.danamon.presentation.auth.register

import android.util.Log
import com.ramdani.danamon.core.base.BaseViewModel
import com.ramdani.danamon.core.base.NetworkHandler
import com.ramdani.danamon.core.enums.UserRole
import com.ramdani.danamon.core.extenstions.disposedBy
import com.ramdani.danamon.core.extenstions.generalErrorServer
import com.ramdani.danamon.core.utils.SingleLiveEvent
import com.ramdani.danamon.data.local.entity.AccountEntity
import com.ramdani.danamon.data.repository.AccountRepository
import io.reactivex.Scheduler

class RegisterVM(
    uiScheduler: Scheduler,
    ioScheduler: Scheduler,
    networkHandler: NetworkHandler,
    private val accountRepository: AccountRepository
) : BaseViewModel(uiScheduler, ioScheduler, networkHandler) {

    val createAccount = SingleLiveEvent<Pair<Boolean,String>>()

    val usernameEvent = SingleLiveEvent<String>()
    val emailEvent = SingleLiveEvent<String>()
    val passwordEvent = SingleLiveEvent<String>()
    val spinnerEvent = SingleLiveEvent<UserRole>()

    val updateAccount = SingleLiveEvent<Pair<Boolean,String>>()

    fun createAccount(accountEntity: AccountEntity) {
        executeJob {
            accountRepository.createAccount(accountEntity)
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

    fun updateAccount(accountEntity: AccountEntity) {
        executeJob {
            accountRepository.updateAccount(accountEntity)
                .compose(applyCompletableSchedulers())
                .doOnSubscribe { isLoadingLiveData.postValue(true) }
                .doOnTerminate { isLoadingLiveData.postValue(false) }
                .subscribe({
                    updateAccount.value = Pair(true,"Update Account Success")
                }, {
                    updateAccount.value = Pair(false,"Update Account Failed!")
                }).disposedBy(disposable)
        }
    }
}