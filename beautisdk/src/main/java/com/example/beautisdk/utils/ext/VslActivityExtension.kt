package com.example.artbeautify.utils.ext

import android.app.Activity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun Activity.hideSystemBar(
    hideStatusBar: Boolean = false,
    hideNavigationBar: Boolean = true,
    isLightStatusBar: Boolean = true,
) {
    val windowInsetsController =
        WindowInsetsControllerCompat(window, window.decorView)
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    if (hideStatusBar) {
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
    }
    if (hideNavigationBar) {
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }
    windowInsetsController.isAppearanceLightStatusBars =
        isLightStatusBar
}