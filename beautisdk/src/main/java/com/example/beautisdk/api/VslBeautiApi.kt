package com.example.beautisdk.api

import android.content.Context
import com.example.beautisdk.api.model.VslBeautiCategoryFeature

internal interface VslBeautiApi {
    fun openFeatureDetail(context: Context, feature: VslBeautiCategoryFeature)
}