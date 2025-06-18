package com.example.beautisdk.api

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.beautisdk.api.model.VslBeautiCategoryFeature
import com.example.beautisdk.ui.screen.art.preview.VslArtPreviewActivity

internal class VslBeautiApiImpl : VslBeautiApi {

    override fun openFeatureDetail(context: Context, feature: VslBeautiCategoryFeature) {
        when (feature) {
            VslBeautiCategoryFeature.AI_ART -> {
                val intent = Intent(context, VslArtPreviewActivity::class.java)
                if (context !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        }
    }
}