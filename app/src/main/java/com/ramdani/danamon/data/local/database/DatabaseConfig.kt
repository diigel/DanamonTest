package com.ramdani.danamon.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ramdani.danamon.data.local.dao.AccountDao
import com.ramdani.danamon.data.local.entity.AccountEntity

@Database(entities = [AccountEntity::class], version = 1)
abstract class DatabaseConfig : RoomDatabase() {
    abstract fun userDao(): AccountDao
}