package com.example.artbeautify.ui.screen.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.beautisdk.ui.screen.art.preview.ArtPreviewActivity
import com.example.beautisdk.ui.screen.splash.VslSplashActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : VslSplashActivity() {
    override fun toNextScreen() {
        val intent = Intent(this, ArtPreviewActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Composable
    override fun UpdateUI(modifier: Modifier) {
    }

}