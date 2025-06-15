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
            prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
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

    @Suppress("UNCHECKED_CAST")
    fun <T> getValue(key: String, default: T): T? {
        if (!::prefs.isInitialized) {
            Log.e(TAG, "Prefs chưa được khởi tạo!")
            return null
        }

        return when (default) {
            is String -> prefs.getString(key, default)
            is Int -> prefs.getInt(key, default)
            is Boolean -> prefs.getBoolean(key, default)
            is Float -> prefs.getFloat(key, default)
            is Long -> prefs.getLong(key, default)
            else -> {
                Log.e(TAG, "Unsupported default type: ${default!!::class.simpleName}")
                null
            }
        } as? T
    }
}