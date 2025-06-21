package com.example.aperoaiservice.utils

import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import com.example.aperoaiservice.AIServiceEntry
import com.example.aperoaiservice.utils.FileHelper.RESOLUTION_IMAGE_OUTPUT
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.max
import kotlin.math.min

suspend fun String.loadBitmapWithCoil(): Bitmap =
    suspendCoroutine { continuation ->
        val context = AIServiceEntry.application
        val request = ImageRequest.Builder(context)
            .data(this)
            .memoryCacheKey(this)
            .diskCacheKey(this)
            .allowHardware(false)
            .target(
                onSuccess = { drawable ->
                    continuation.resume(drawable.toBitmap())
                },
                onError = {
                    // fallback nếu muốn
                }
            )
            .build()

        context.imageLoader.enqueue(request)
    }


suspend fun String.loadBitmapAndScaleWithCoil(imageWidth: Int, imageHeight: Int): Bitmap =
    suspendCoroutine { continuation ->
        val context = AIServiceEntry.application
        val size =
            (RESOLUTION_IMAGE_OUTPUT.toDouble() / max(imageWidth, imageHeight)) * min(imageWidth, imageHeight)

        val request = ImageRequest.Builder(context)
            .data(this)
            .memoryCacheKey(this)
            .diskCacheKey(this)
            .size(size.toInt()) // tương đương .override(size)
            .allowHardware(false)
            .target(
                onSuccess = { drawable ->
                    continuation.resume(drawable.toBitmap())
                },
                onError = {
                    // fallback nếu cần
                }
            )
            .build()

        context.imageLoader.enqueue(request)
    }
