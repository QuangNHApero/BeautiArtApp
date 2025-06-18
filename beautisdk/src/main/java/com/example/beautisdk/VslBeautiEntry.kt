package com.example.beautisdk

import android.content.Context
import com.example.beautisdk.api.VslBeautiApi
import com.example.beautisdk.api.VslBeautiApiImpl
import com.example.beautisdk.api.config.DefaultSubFeatureConfig
import com.example.beautisdk.api.config.VslBeautiCommonConfig
import com.example.beautisdk.api.config.VslBeautiConfig
import com.example.beautisdk.api.config.VslBeautiServiceConfig
import com.example.beautisdk.api.config.VslBeautyFullSubFeatureConfig
import com.example.beautisdk.api.config.subfeature.art.VslArtFeatureConfig
import com.example.beautisdk.api.config.subfeature.art.VslDefaultArtFeatureConfig
import com.example.beautisdk.api.config.subfeature.pick_photo.VslDefaultPickPhotoConfig
import com.example.beautisdk.api.config.subfeature.pick_photo.VslPickPhotoFeatureConfig
import com.example.beautisdk.api.config.subfeature.result.VslDefaultResultConfig
import com.example.beautisdk.api.config.subfeature.result.VslResultFeatureConfig
import com.example.beautisdk.api.config.ui.VslBeautiUiConfig
import com.example.beautisdk.api.config.ui.VslDefaultUiConfig
import com.example.beautisdk.api.model.VslBeautiCategoryFeature
import com.example.beautisdk.utils.pref.VslSharedPref

object VslBeautiEntry {

    @Volatile
    private var _config: VslBeautiConfig? = null
    internal var api: VslBeautiApi = VslBeautiApiImpl()

    @JvmStatic
    fun init(
        context: Context,
        common: CommonConfigBuilder.() -> Unit,
        service: VslBeautiServiceConfig,
        builder: SubFeatureBuilder.() -> Unit = {}
    ) {
        if (_config != null) return

        val commonConfig = CommonConfigBuilder("").apply(common).build()
        val customSub = SubFeatureBuilder().apply(builder).build()

        _config = object : VslBeautiConfig,
            VslBeautiCommonConfig by commonConfig,
            VslBeautiServiceConfig by service,
            VslBeautyFullSubFeatureConfig by customSub {}

        VslSharedPref.initialize(context)
    }

    internal fun config(): VslBeautiConfig =
        _config ?: error("VslBeautiEntry.init() must be called before using the library")


    fun openFeatureDetail(context: Context, feature: VslBeautiCategoryFeature) {
        if (_config == null) return
        api.openFeatureDetail(context, feature)
    }

    class SubFeatureBuilder {

        var fullConfig: VslBeautyFullSubFeatureConfig? = null

        var artConfig: VslArtFeatureConfig? = null

        var resultConfig: VslResultFeatureConfig? = null

        var pickPhotoConfig: VslPickPhotoFeatureConfig? = null

        internal fun build(): VslBeautyFullSubFeatureConfig {
            fullConfig?.let { return it }

            val artDelegate: VslArtFeatureConfig =
                artConfig ?: VslDefaultArtFeatureConfig

            val resultDelegate: VslResultFeatureConfig =
                resultConfig ?: VslDefaultResultConfig

            val pickDelegate: VslPickPhotoFeatureConfig =
                pickPhotoConfig ?: VslDefaultPickPhotoConfig

            return object : DefaultSubFeatureConfig(),
                VslArtFeatureConfig          by artDelegate,
                VslResultFeatureConfig       by resultDelegate,
                VslPickPhotoFeatureConfig    by pickDelegate {}
        }
    }

    class CommonConfigBuilder(
        var appName: String
    ) {
        var languageCode: String = "en"
        var uiConfig: VslBeautiUiConfig? = null

        fun build(): VslBeautiCommonConfig {
            val finalUiConfig = uiConfig ?: VslDefaultUiConfig
            return object : VslBeautiCommonConfig {
                override val appName = this@CommonConfigBuilder.appName
                override val languageCode = this@CommonConfigBuilder.languageCode
                override val uiConfig = finalUiConfig
            }
        }
    }
}