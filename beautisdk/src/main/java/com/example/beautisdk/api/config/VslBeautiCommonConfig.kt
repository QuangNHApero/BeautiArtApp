package com.example.beautisdk.api.config

import com.example.beautisdk.api.config.ui.VslBeautiUiConfig

interface VslBeautiCommonConfig {
    val appName: String
    val languageCode: String get() = "en"
    val uiConfig: VslBeautiUiConfig
}