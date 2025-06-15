package com.example.artbeautify.ui.screen.splash

import android.annotation.SuppressLint
import android.content.Intent
import com.example.beautisdk.ui.screen.art.preview.ArtPreviewActivity
import com.example.beautisdk.ui.screen.splash.VslSplashActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : VslSplashActivity() {
    override fun toNextScreen() {
        val intent = Intent(this, ArtPreviewActivity::class.java)
        startActivity(intent)
        finish()
    }
}