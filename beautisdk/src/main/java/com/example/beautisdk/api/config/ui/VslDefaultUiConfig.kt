package com.example.beautisdk.api.config.ui

import androidx.compose.ui.graphics.Color
import com.example.beautisdk.R

object VslDefaultUiConfig : VslBeautiUiConfig {
    override val backgroundColor = Color.White
    override val borderColor = Color(0xFFE400D9)
    override val foregroundLogo = R.drawable.ic_launcher_foreground
    override val backgroundLogo = R.drawable.ic_launcher_background
    override val loadingRawId = R.raw.vsl_anim_loading
    override val btnContent = VslBtnContent(
        startColor = Color(0xFFE400D9),
        endColor = Color(0xFF0000FF)
    )
}