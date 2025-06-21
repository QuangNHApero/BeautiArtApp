package com.example.beautisdk.api.config

interface VslBeautiCommonConfig {
    val appName: String
    val applicationId: String
    val languageCode: String get() = "en"
}