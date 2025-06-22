package com.example.beautisdk.ui.screen.splash

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.beautisdk.base.BaseActivity
import com.example.beautisdk.ui.design_system.pxToDp
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
abstract class VslSplashActivity : BaseActivity() {
    private val viewModel: VslSplashViewModel by viewModel()
    final override fun onBackNavigation() {
        finish()
    }

    @Composable
    override fun UpdateUI(modifier: Modifier) {
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            viewModel.effect.collect {
                when (it) {
                    is VslSplashEffect.NavigateToNextActivity -> {
                        toNextScreen()
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            viewModel.preloadDatas(context)
        }
        MainContent(modifier)
    }

    @Composable
    private fun MainContent(modifier: Modifier = Modifier) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(
                config.loadingRawId
            )
        )

        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )

        Column(
            modifier = modifier
                .fillMaxSize(),
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
                painter = painterResource(id = config.backgroundLogo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(22.pxToDp())
                    )
            )

            Image(
                painter = painterResource(id = config.foregroundLogo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    protected abstract fun toNextScreen()
}