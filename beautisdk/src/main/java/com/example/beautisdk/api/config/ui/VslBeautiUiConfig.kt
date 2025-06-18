package com.example.beautisdk.api.config.ui

import androidx.compose.ui.graphics.Color

interface VslBeautiUiConfig {
    val backgroundColor: Color
    val borderColor: Color
    val foregroundLogo : Int
    val backgroundLogo : Int
    val loadingRawId : Int
    val btnContent : VslBtnContent
}


data class VslBtnContent(
    val startColor: Color,
    val endColor: Color,
)

