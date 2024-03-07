package com.ramdani.danamon.data.repository

import com.ramdani.danamon.data.local.entity.UserEntity
import io.reactivex.Completable
import io.reactivex.Single

interface UserRepository {

    fun getAccount(username: String, password: String): Single<UserEntity>

    fun createAccount(userEntity : UserEntity) : Completable
}