package com.example.beautisdk.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.example.beautisdk.utils.pref.VslSharedPref
import com.example.beautisdk.utils.pref.VslSharedPrefConst.STORAGE_WRITE_PERMISSION_ATTEMPT

internal object PermissionUtil {
    const val STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE"
    private const val MAX_ATTEMPTS = 2

    private fun isAtLeastQ(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    private fun hasWritePermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun shouldRequestWritePermission(): Boolean {
        return !isAtLeastQ()
    }

    fun checkAndRequestWritePermission(
        context: Context,
        launcher: ActivityResultLauncher<String>,
    ) {
        if (shouldRequestWritePermission()) {
            if (!hasWritePermission(context)) {
                val permissionAttempt = VslSharedPref.getValue(STORAGE_WRITE_PERMISSION_ATTEMPT, 0) ?: 0
                if (permissionAttempt >= MAX_ATTEMPTS) {
                    openAppSettings(context)
                } else {
                    VslSharedPref.putValue(STORAGE_WRITE_PERMISSION_ATTEMPT, permissionAttempt + 1)
                    launcher.launch(STORAGE_PERMISSION)
                }
            }
        }
    }

    private fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}