package com.ramdani.danamon.core.base

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

    companion object {
        private const val USER_ID = "userId"
        private const val USER_NAME = "userName"
        private const val PASSWORD = "Password"
        private const val IS_ACTIVE = "isActive"
    }

    var userName: String?
        get() = sharedPreferences.getString(USER_NAME, null)
        set(value) = sharedPreferences.edit().putString(USER_NAME, value).apply()

    var userId: Int?
        get() = sharedPreferences.getInt(USER_ID, -1)
        set(value) = sharedPreferences.edit().putInt(USER_ID, value ?: -1).apply()

    var password: String?
        get() = sharedPreferences.getString(PASSWORD, null)
        set(value) = sharedPreferences.edit().putString(PASSWORD, value).apply()

    var isActive: Boolean
        get() = sharedPreferences.getBoolean(IS_ACTIVE, false)
        set(value) = sharedPreferences.edit().putBoolean(IS_ACTIVE, value).apply()
}