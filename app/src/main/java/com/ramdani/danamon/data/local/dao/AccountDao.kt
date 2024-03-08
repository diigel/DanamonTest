package com.ramdani.danamon.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ramdani.danamon.core.enums.UserRole
import com.ramdani.danamon.data.local.entity.AccountEntity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createAccount(accountEntity : AccountEntity) : Completable

    @Query("SELECT * FROM table_account WHERE username = :username AND password = :password limit 1")
    fun getAccount(username: String, password: String): Single<AccountEntity>

    @Query("SELECT * FROM table_account WHERE username =:username AND role = :role")
    fun getAccountByRole(username: String,role: UserRole) : Maybe<AccountEntity>

    @Query("SELECT * FROM table_account")
    fun getAllAccount(): Single<List<AccountEntity>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAccount(accountEntity: AccountEntity): Completable

    @Delete
    fun deleteAccount(user: AccountEntity): Completable

    @Query("UPDATE table_account SET isActive = :newValue WHERE id = :userId")
    fun updateIsActive(userId: Int, newValue: Boolean) : Completable

}