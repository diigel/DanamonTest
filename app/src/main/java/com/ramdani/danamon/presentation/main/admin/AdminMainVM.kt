package com.ramdani.danamon.presentation.main.admin

import com.ramdani.danamon.core.base.BaseViewModel
import com.ramdani.danamon.core.base.NetworkHandler
import com.ramdani.danamon.core.base.SharedPreferencesHelper
import com.ramdani.danamon.core.extenstions.disposedBy
import com.ramdani.danamon.core.extenstions.generalErrorServer
import com.ramdani.danamon.core.utils.SingleLiveEvent
import com.ramdani.danamon.data.local.entity.AccountEntity
import com.ramdani.danamon.data.repository.AccountRepository
import io.reactivex.Scheduler

class AdminMainVM(
    uiSchedulers: Scheduler,
    ioSchedulers: Scheduler,
    networkHandler: NetworkHandler,
    private val accountRepository: AccountRepository,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : BaseViewModel(
    uiSchedulers = uiSchedulers,
    ioScheduler = ioSchedulers,
    networkHandler = networkHandler
) {

    val getAllAccount = SingleLiveEvent<List<AccountEntity>>()
    val deletedAccount = SingleLiveEvent<Pair<Boolean, String>>()
    val updateActiveAccountEvent = SingleLiveEvent<Boolean>()

    fun getAllAccount() {
        executeJob {
            accountRepository.getAllAccount()
                .compose(applySchedulers())
                .doOnSubscribe { isLoadingLiveData.postValue(true) }
                .doFinally { isLoadingLiveData.postValue(false) }
                .subscribe({ account ->
                    getAllAccount.value = account.filter { it.id != sharedPreferencesHelper.userId }
                }, {
                    handleFailure(it.generalErrorServer())
                }).disposedBy(disposable)
        }
    }

    fun deleteAccount(accountEntity: AccountEntity) {
        executeJob {
            accountRepository.deletedAccount(accountEntity)
                .compose(applyCompletableSchedulers())
                .doOnSubscribe { isLoadingLiveData.postValue(true) }
                .doOnTerminate { isLoadingLiveData.postValue(false) }
                .subscribe({
                    deletedAccount.value = Pair(true, "Deleted Account Success")
                }, {
                    deletedAccount.value = Pair(false, "Deleted Account Failed!")
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