package com.example.beautisdk.api.config.subfeature.pick_photo

import com.example.beautisdk.R

object VslDefaultPickPhotoConfig : VslPickPhotoFeatureConfig {
    override val closeBtnResId: Int
        get() = R.drawable.ic_close
    override val selectedBtn: Int
        get() = R.drawable.ic_pickphoto_selected
    override val unSelectedBtn: Int
        get() = R.drawable.ic_pickphoto_unselected
}