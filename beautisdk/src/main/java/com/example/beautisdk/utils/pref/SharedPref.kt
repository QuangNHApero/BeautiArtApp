package com.example.beautisdk.utils.pref

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharedPref {
    private const val PREF_NAME = "beauti_pref"
    private const val TAG = "beauti_pref"
    private lateinit var prefs: SharedPreferences

    fun initialize(context: Context) {
        if (!::prefs.isInitialized) {
            prefs = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }
    }

    fun putValue(key: String, value: Any) {
        if (!::prefs.isInitialized) {
            Log.e(TAG, "Prefs chưa được khởi tạo!")
            return
        }
        with(prefs.edit()) {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                else -> {
                    Log.e(TAG, "Unsupported type: ${value::class.simpleName}")
                    return
                }
            }
            apply()
        }
    }

    fun <T> getValue(key: String, default: T): T {
        if (!::prefs.isInitialized) {
            Log.e(TAG, "Prefs chưa được khởi tạo!")
            return default
        }

        return when (default) {
            is String  -> prefs.getString(key, default) as T
            is Int     -> prefs.getInt(key, default)     as T
            is Boolean -> prefs.getBoolean(key, default) as T
            is Float   -> prefs.getFloat(key, default)   as T
            is Long    -> prefs.getLong(key, default)    as T
            else -> default
        }
    }
}