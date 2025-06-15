package com.example.beautisdk.ui.design_system

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalScreenScale = compositionLocalOf { 1f }

@Composable
fun Number.pxToDpCompose(): Dp {
    val scale = LocalScreenScale.current
    return (this.toFloat() * scale).dp
}

fun Number.pxToDp(): Dp {
    val scale = cachedDensityScale
    return (this.toFloat() * scale).dp
}
