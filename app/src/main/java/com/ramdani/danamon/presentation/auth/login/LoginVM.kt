package com.ramdani.danamon.presentation.auth.login

import androidx.room.EmptyResultSetException
import com.ramdani.danamon.core.base.BaseViewModel
import com.ramdani.danamon.core.base.NetworkHandler
import com.ramdani.danamon.core.base.SharedPreferencesHelper
import com.ramdani.danamon.core.extenstions.disposedBy
import com.ramdani.danamon.core.extenstions.generalErrorServer
import com.ramdani.danamon.core.utils.Failure
import com.ramdani.danamon.core.utils.SingleLiveEvent
import com.ramdani.danamon.data.local.entity.AccountEntity
import com.ramdani.danamon.data.repository.AccountRepository
import io.reactivex.Scheduler

class LoginVM(
    uiScheduler: Scheduler,
    ioScheduler: Scheduler,
    networkHandler: NetworkHandler,
    private val accountRepository: AccountRepository,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : BaseViewModel(uiScheduler, ioScheduler, networkHandler) {

    val getAccount = SingleLiveEvent<AccountEntity>()

    fun getAccount(userName : String,password : String) {
        executeJob {
            accountRepository.getAccount(userName,password)
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

    fun saveUserId(userId: Int) {
        sharedPreferencesHelper.userId = userId
    }

    fun saveIsActive(isActive: Boolean) {
        sharedPreferencesHelper.isActive = isActive
    }

    fun savePassword(password: String) {
        sharedPreferencesHelper.password = password
    }

    fun saveUsername(userName: String) {
        sharedPreferencesHelper.userName = userName
    }
}