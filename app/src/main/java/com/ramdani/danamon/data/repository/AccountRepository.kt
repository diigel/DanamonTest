package com.ramdani.danamon.data.repository

import com.ramdani.danamon.data.local.entity.AccountEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface AccountRepository {

    fun getAccount(username: String, password: String): Single<AccountEntity>

    fun createAccount(accountEntity : AccountEntity) : Completable

    fun getAllAccount() : Single<List<AccountEntity>>

    fun updateAccount(accountEntity: AccountEntity) : Completable

    fun deletedAccount(accountEntity: AccountEntity) : Completable

    fun updateActiveAccount(userId : Int,isActive : Boolean) : Completable
}