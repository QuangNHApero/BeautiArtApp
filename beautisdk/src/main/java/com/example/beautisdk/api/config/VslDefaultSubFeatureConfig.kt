package com.example.beautisdk.api.config

import com.example.beautisdk.api.config.subfeature.art.VslArtFeatureConfig
import com.example.beautisdk.api.config.subfeature.art.VslDefaultArtFeatureConfig
import com.example.beautisdk.api.config.subfeature.base.VslBeautiBaseConfig
import com.example.beautisdk.api.config.subfeature.base.VslDefaultBaseConfig
import com.example.beautisdk.api.config.subfeature.pick_photo.VslDefaultPickPhotoConfig
import com.example.beautisdk.api.config.subfeature.pick_photo.VslPickPhotoFeatureConfig
import com.example.beautisdk.api.config.subfeature.result.VslDefaultResultConfig
import com.example.beautisdk.api.config.subfeature.result.VslResultFeatureConfig

open class DefaultSubFeatureConfig :
    VslBeautyFullSubFeatureConfig,
    VslBeautiBaseConfig by VslDefaultBaseConfig,
    VslArtFeatureConfig by VslDefaultArtFeatureConfig,
    VslResultFeatureConfig by VslDefaultResultConfig,
    VslPickPhotoFeatureConfig by VslDefaultPickPhotoConfig