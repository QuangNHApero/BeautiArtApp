package com.example.beautisdk.ui.screen.pick_photo.data

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
data class PhotoItem(
    val id: Long,
    val uri: Uri
)
