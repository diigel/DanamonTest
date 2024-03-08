package com.ramdani.danamon.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ramdani.danamon.core.enums.UserRole
import kotlinx.parcelize.Parcelize

@Entity(tableName = "table_account")
@Parcelize
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("username")
    val username: String,
    @ColumnInfo("email")
    val email: String,
    @ColumnInfo("password")
    val password: String,
    @ColumnInfo("role")
    val role : UserRole,
    @ColumnInfo("isActive")
    val isActive : Boolean
) : Parcelable
