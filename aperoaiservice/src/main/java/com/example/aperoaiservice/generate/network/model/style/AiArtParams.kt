package com.example.aperoaiservice.generate.network.model.style

import androidx.annotation.IntRange
import kotlin.random.Random

data class AiArtParams(
    val pathImageOrigin: String,
    val styleId: String? = null,
    val style: String? = null,
    val positivePrompt: String? = null,
    val negativePrompt: String? = null,
    @IntRange(256,2048)
    val imageSize: Int? = null,
    @IntRange(1, 1280)
    val fixHeight: Int? = null,
    @IntRange(1, 1024)
    val fixWidth: Int? = null,
    val fixWidthAndHeight: Boolean? = null,
    val useControlnet: Boolean? = null,
    val applyPulid: Boolean? = null,
    @IntRange(1,100000)
    val seed: Int? = Random.nextInt(1, 100000),
    val fastMode: Boolean? = null
)