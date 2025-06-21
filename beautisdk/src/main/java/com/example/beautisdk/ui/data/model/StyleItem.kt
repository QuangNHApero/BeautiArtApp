package com.example.beautisdk.ui.data.model

import com.google.gson.annotations.SerializedName

data class StyleArt(
    val _id : String,
    val name: String,
    @SerializedName("key")
    val thumbnail: String,
    val positivePrompt: String?,
    val negativePrompt: String?,
    val mode: String?,
    val basemodel: String?
)