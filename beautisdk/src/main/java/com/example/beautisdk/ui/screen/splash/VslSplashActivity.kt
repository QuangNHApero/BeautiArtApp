package com.example.beautisdk.ui.screen.splash

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.beautisdk.base.BaseActivity
import com.example.beautisdk.utils.ImageHandlerUtil
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@SuppressLint("CustomSplashScreen")
abstract class VslSplashActivity : BaseActivity() {
    internal val viewModel: VslSplashViewModel by viewModels()
    final override fun onBackNavigation() {
        finish()
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.isLoading
            .onEach { isLoading ->
                if (!isLoading) {
                    toNextScreen()
                }
            }.launchIn(lifecycleScope)

    }


    @Composable
    override fun UpdateUI(modifier: Modifier) {
        val density = LocalDensity.current
        val screenWidthPx = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
        val targetSize = (screenWidthPx - 50).toInt()

        LaunchedEffect(Unit) {
            ImageHandlerUtil.preload(
                context = this@VslSplashActivity,
                uri = Uri.parse("https://dev-static.apero.vn/video-editor-pro/ai-style-thumbnail/1701939921-M878.png"),
                widthPx = targetSize,
                heightPx = targetSize
            )
            viewModel.setLoading(false)
        }
    }

    protected abstract fun toNextScreen()
}