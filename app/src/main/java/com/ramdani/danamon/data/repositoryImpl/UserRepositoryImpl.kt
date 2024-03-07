package com.ramdani.danamon.data.repositoryImpl

import com.ramdani.danamon.data.local.dao.UserDao
import com.ramdani.danamon.data.local.entity.UserEntity
import com.ramdani.danamon.data.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Single

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override fun getAccount(username: String, password: String): Single<UserEntity> {
        return userDao.getAccount(username, password)
    }

    override fun createAccount(userEntity: UserEntity): Completable {
        return userDao.getAccountByRole(userEntity.username, userEntity.role)
            .isEmpty
            .flatMapCompletable { isEmpty ->
                if (isEmpty) userDao.createAccount(userEntity)
                else Completable.error(Throwable("Account already exists"))
            }
    }
}