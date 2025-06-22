package com.example.beautisdk

import android.app.Application
import android.content.Context
import com.example.aperoaiservice.AIServiceEntry
import com.example.beautisdk.api.VslBeautiApi
import com.example.beautisdk.api.VslBeautiApiImpl
import com.example.beautisdk.api.config.VslBeautiCommonConfig
import com.example.beautisdk.api.config.VslBeautiConfig
import com.example.beautisdk.api.config.VslBeautiServiceConfig
import com.example.beautisdk.api.config.VslBeautyFullSubFeatureConfig
import com.example.beautisdk.api.config.subfeature.art.VslArtFeatureConfig
import com.example.beautisdk.api.config.subfeature.art.VslDefaultArtFeatureConfig
import com.example.beautisdk.api.config.subfeature.base.VslBaseConfig
import com.example.beautisdk.api.config.subfeature.base.VslDefaultBaseConfig
import com.example.beautisdk.api.config.subfeature.pick_photo.VslDefaultPickPhotoConfig
import com.example.beautisdk.api.config.subfeature.pick_photo.VslPickPhotoFeatureConfig
import com.example.beautisdk.api.config.subfeature.result.VslDefaultResultConfig
import com.example.beautisdk.api.config.subfeature.result.VslResultFeatureConfig
import com.example.beautisdk.api.model.VslBeautiCategoryFeature
import com.example.beautisdk.di.appModule
import com.example.beautisdk.utils.KoinUtils
import com.example.beautisdk.utils.pref.VslSharedPref

object VslBeautiEntry {

    @Volatile
    private var _config: VslBeautiConfig? = null
    internal var api: VslBeautiApi = VslBeautiApiImpl()

    @JvmStatic
    fun init(
        application: Application,
        common: VslBeautiCommonConfig,
        service: VslBeautiServiceConfig,
        builder: SubFeatureBuilder.() -> Unit = {}
    ) {
        if (_config != null) return

        val customSub = SubFeatureBuilder().apply(builder).build()

        _config = object : VslBeautiConfig,
            VslBeautiCommonConfig by common,
            VslBeautiServiceConfig by service,
            VslBeautyFullSubFeatureConfig by customSub {}

        AIServiceEntry.initialize(
            projectName = common.appName,
            applicationId = common.applicationId,
            apiKey = service.apiKey,
            application = application
        )

        KoinUtils.startKoinIfNeeded(
            application,
            listOf(
                appModule
            )
        )


        VslSharedPref.initialize(application.applicationContext)
    }

    internal fun config(): VslBeautiConfig =
        _config ?: error("VslBeautiEntry.init() must be called before using the library")


    fun openFeatureDetail(context: Context, feature: VslBeautiCategoryFeature) {
        if (_config == null) return
        api.openFeatureDetail(context, feature)
    }

    class SubFeatureBuilder {

        var fullConfig: VslBeautyFullSubFeatureConfig? = null

        var baseConfig: VslBaseConfig? = null

        var artConfig: VslArtFeatureConfig? = null

        var resultConfig: VslResultFeatureConfig? = null

        var pickPhotoConfig: VslPickPhotoFeatureConfig? = null

        internal fun build(): VslBeautyFullSubFeatureConfig {
            fullConfig?.let { provided ->
                require(
                    baseConfig == null &&
                            artConfig  == null &&
                            resultConfig == null &&
                            pickPhotoConfig == null
                ) { "When fullConfig is set, the other fragments must be null." }

                return provided
            }

            val base = baseConfig ?: VslDefaultBaseConfig
            val art  = artConfig ?: VslDefaultArtFeatureConfig
            val result = resultConfig ?: VslDefaultResultConfig
            val pick = pickPhotoConfig ?: VslDefaultPickPhotoConfig

            return object : VslBeautyFullSubFeatureConfig,
                VslBaseConfig by base,
                VslArtFeatureConfig by art,
                VslResultFeatureConfig by result,
                VslPickPhotoFeatureConfig by pick {}
        }
    }
}