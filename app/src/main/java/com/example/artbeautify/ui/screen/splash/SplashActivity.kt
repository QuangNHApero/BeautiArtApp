package com.example.artbeautify.ui.screen.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.artbeautify.base.BaseActivity
import com.example.beautisdk.ui.screen.preview.PreviewActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity(){
    private val viewModel: SplashViewModel by viewModels()
    override fun onBackNavigation() {
        finish()
    }

    @Composable
    override fun UpdateUI(modifier: Modifier) {
        val isLoading = viewModel.isLoading.collectAsState()

        LaunchedEffect(isLoading.value) {
            if (!isLoading.value) {
                toNextScreen()
            }
        }
    }

    private fun toNextScreen() {
        val intent = Intent(this, PreviewActivity::class.java)
        startActivity(intent)
        finish()
    }
}