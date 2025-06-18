package com.example.beautisdk.ui.screen.splash

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.beautisdk.R
import com.example.beautisdk.base.BaseActivity
import com.example.beautisdk.ui.design_system.pxToDp
import com.example.beautisdk.utils.VslImageHandlerUtil
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@SuppressLint("CustomSplashScreen")
abstract class VslSplashActivity : BaseActivity() {
    private val viewModel: VslSplashViewModel by viewModels()
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
            VslImageHandlerUtil.preload(
                context = this@VslSplashActivity,
                uri = Uri.parse("https://dev-static.apero.vn/video-editor-pro/ai-style-thumbnail/1701939921-M878.png"),
                widthPx = targetSize,
                heightPx = targetSize
            )
            viewModel.setLoading(false)
        }
        MainContent(modifier)
    }

    @Composable
    private fun MainContent(modifier: Modifier = Modifier) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                loadingAnimation
            )
        )

        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )

        Column(
            modifier = modifier.fillMaxSize().background(backgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LogoApp(
                modifier = Modifier.size(200.pxToDp())
            )

            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(120.pxToDp())
            )
        }
    }

    @Composable
    fun LogoApp(modifier: Modifier = Modifier) {
        Box(
            modifier = modifier.fillMaxSize(),
        ) {
            Image(
                painter = painterResource(id = config.uiConfig.backgroundLogo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(22.pxToDp())
                    )
            )

            Image(
                painter = painterResource(id = config.uiConfig.foregroundLogo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    protected abstract fun toNextScreen()
    protected val backgroundColor: Color = Color.White
    protected val loadingAnimation: Int = R.raw.vsl_anim_loading
}