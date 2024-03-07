package com.ramdani.danamon.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ramdani.danamon.data.local.dao.UserDao
import com.ramdani.danamon.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class DatabaseConfig : RoomDatabase() {
    abstract fun userDao(): UserDao
}