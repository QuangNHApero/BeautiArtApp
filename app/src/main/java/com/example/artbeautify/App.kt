package com.example.artbeautify

import android.app.Application
import com.example.beautisdk.utils.pref.SharedPref

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPref.initialize(this)
    }
}