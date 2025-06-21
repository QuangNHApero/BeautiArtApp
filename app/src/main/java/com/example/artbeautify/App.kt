package com.example.artbeautify

import android.app.Application
import com.example.artbeautify.utils.initialize.BeautiServiceConfig
import com.example.beautisdk.VslBeautiEntry

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        VslBeautiEntry.init(
            this, common = {
            appName = "ArtBeautify"
        }, BeautiServiceConfig())
    }
}