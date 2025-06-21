package com.example.beautisdk.api.config

import com.example.beautisdk.api.config.subfeature.art.VslArtFeatureConfig
import com.example.beautisdk.api.config.subfeature.base.VslBaseConfig
import com.example.beautisdk.api.config.subfeature.pick_photo.VslPickPhotoFeatureConfig
import com.example.beautisdk.api.config.subfeature.result.VslResultFeatureConfig

interface VslBeautyFullSubFeatureConfig : VslBaseConfig, VslArtFeatureConfig, VslResultFeatureConfig,
    VslPickPhotoFeatureConfig