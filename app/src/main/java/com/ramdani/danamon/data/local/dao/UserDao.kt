package com.ramdani.danamon.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ramdani.danamon.core.enums.UserRole
import com.ramdani.danamon.data.local.entity.UserEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun createAccount(userEntity : UserEntity) : Completable

    @Query("SELECT * FROM table_account WHERE username = :username AND password = :password limit 1")
    fun getAccount(username: String, password: String): Single<UserEntity>

    @Query("SELECT * FROM table_account WHERE username =:username AND role = :role")
    fun getAccountByRole(username: String,role: UserRole) : Maybe<UserEntity>

    @Query("delete from table_account")
    fun truncateTableAccount()
}