package com.ramdani.danamon.presentation.splash

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

class SplashVM(
    uiSchedulers: Scheduler,
    ioSchedulers: Scheduler,
    networkHandler: NetworkHandler,
    private val accountRepository: AccountRepository,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) :
    BaseViewModel(uiSchedulers, ioSchedulers, networkHandler) {

    val getAccount = SingleLiveEvent<AccountEntity>()

    fun getAccount() {
        executeJob {
            accountRepository.getAccount(getUserName(), getPassword())
                .compose(applySchedulers())
                .subscribe({ user ->
                    getAccount.value = user
                }, {
                    if (it is EmptyResultSetException) handleFailure(Failure.DataNotExist("User Not Found")) else handleFailure(it.generalErrorServer())
                }).disposedBy(disposable)

        }
    }

     fun getUserName(): String = sharedPreferencesHelper.userName.orEmpty()

     fun getPassword(): String = sharedPreferencesHelper.password.orEmpty()

}