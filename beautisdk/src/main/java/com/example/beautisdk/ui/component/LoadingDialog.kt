package com.example.beautisdk.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.beautisdk.R
import com.example.beautisdk.ui.design_system.AppTheme
import com.example.beautisdk.ui.design_system.LocalCustomTypography
import com.example.beautisdk.ui.design_system.component.AperoTextView
import com.example.beautisdk.ui.design_system.pxToDp

@Composable
fun LoadingDialog(
    loadingResId: Int = R.raw.vsl_anim_loading,
    colorDialog: Color = Color.White,
    textResId: Int = R.string.loading_text,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            loadingResId
        )
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable {  },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(30.pxToDp()))
                .background(colorDialog)
                .widthIn(max = 200.pxToDp())
                .padding(horizontal = 38.pxToDp(), vertical = 18.pxToDp())
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(120.pxToDp())
                )

                AperoTextView(
                    text = stringResource(textResId),
                    textStyle = LocalCustomTypography.current.Body.bold.copy(color = Color.Black),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 10.pxToDp())
                )
            }
        }
    }
}

@Preview(
    name = "Loading Dialog",
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    widthDp = 360,
    heightDp = 640
)
@Composable
fun PreviewLoadingDialog() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()){
            LoadingDialog(modifier = Modifier.fillMaxSize())
        }
    }
}