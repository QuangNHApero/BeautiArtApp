package com.example.artbeautify.ui.screen.splash

import android.annotation.SuppressLint
import com.example.beautisdk.VslBeautiEntry
import com.example.beautisdk.api.model.VslBeautiCategoryFeature
import com.example.beautisdk.ui.screen.splash.VslSplashActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : VslSplashActivity() {
    override fun toNextScreen() {
        VslBeautiEntry.openFeatureDetail(this, VslBeautiCategoryFeature.AI_ART)
        finish()
    }
}