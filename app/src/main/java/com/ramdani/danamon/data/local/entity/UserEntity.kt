package com.ramdani.danamon.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ramdani.danamon.core.enums.UserRole
import kotlinx.parcelize.Parcelize

@Entity(tableName = "table_account")
@Parcelize
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val email: String,
    val password: String,
    val role : UserRole
) : Parcelable
