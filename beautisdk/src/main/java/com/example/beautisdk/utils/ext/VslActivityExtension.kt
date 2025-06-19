package com.example.artbeautify.utils.ext

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    } else {
        // For devices < Android M
        val networkInfo = connectivityManager.activeNetworkInfo
        networkInfo != null && networkInfo.isConnected
    }
}