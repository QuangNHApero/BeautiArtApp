package com.example.artbeautify.utils.initialize

import com.example.beautisdk.api.config.VslBeautiCommonConfig

class BeautiCommonConfig : VslBeautiCommonConfig {
    override val appName: String
        get() = "Tecktrek"
    override val applicationId: String
        get() = "for.techtrek"
}