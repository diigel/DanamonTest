package com.ramdani.danamon.data.repositoryImpl

import com.ramdani.danamon.data.local.dao.AccountDao
import com.ramdani.danamon.data.local.entity.AccountEntity
import com.ramdani.danamon.data.repository.AccountRepository
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class AccountRepositoryImpl(private val accountDao: AccountDao) : AccountRepository {
    override fun getAccount(username: String, password: String): Single<AccountEntity> {
        return accountDao.getAccount(username, password)
            .flatMap { account ->
                if (!account.isActive) {
                    accountDao.updateIsActive(account.id, true)
                        .andThen(Single.just(account.copy(isActive = true)))
                } else {
                    Single.just(account)
                }
            }
    }

    override fun createAccount(accountEntity: AccountEntity): Completable {
        return accountDao.getAccountByRole(accountEntity.username, accountEntity.role)
            .isEmpty
            .flatMapCompletable { isEmpty ->
                if (isEmpty) accountDao.createAccount(accountEntity)
                else Completable.error(Throwable("Account already exists"))
            }
    }

    override fun getAllAccount(): Single<List<AccountEntity>> {
        return accountDao.getAllAccount()
    }

    override fun updateAccount(accountEntity: AccountEntity): Completable {
        return accountDao.updateAccount(accountEntity).subscribeOn(Schedulers.io())
    }

    override fun deletedAccount(accountEntity: AccountEntity): Completable {
        return accountDao.deleteAccount(accountEntity)
    }

    override fun updateActiveAccount(userId: Int, isActive: Boolean): Completable {
        return accountDao.updateIsActive(userId,isActive)
    }
}