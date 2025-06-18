package com.example.beautisdk.ui.screen.pick_photo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautisdk.ui.design_system.pxToDp
import com.example.beautisdk.ui.screen.pick_photo.data.PhotoItem
import com.example.beautisdk.utils.PermissionUtil
import com.example.beautisdk.utils.VslImageHandlerUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class VslPickPhotoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        VslPickPhotoUiState(
            photos = VslImageHandlerUtil.cachedPhotos.toList()
        )
    )
    val uiState: StateFlow<VslPickPhotoUiState> = _uiState.asStateFlow()
    private var currentOffset = VslImageHandlerUtil.cachedPhotos.size
    private val batchSize = 15
    private var isLoadingMore = false

    private val _effect = Channel<VslPickPhotoUiEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: VslPickPhotoEvent) {
        viewModelScope.launch {
            when (event) {
                is VslPickPhotoEvent.OnBackClicked -> {
                    _effect.send(VslPickPhotoUiEffect.BackNavigation)
                }
                is VslPickPhotoEvent.OnNextClicked -> {
                    _uiState.value.selectedPhoto?.let {
                        _effect.send(VslPickPhotoUiEffect.NextNavigation(it))
                    }
                }
                is VslPickPhotoEvent.OnPhotoSelected -> {
                    if (_uiState.value.selectedPhoto != null) {
                        _uiState.value = _uiState.value.copy(
                            selectedPhoto = event.photo
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            selectedPhoto = event.photo,
                            isNextEnabled = true
                        )
                    }
                }
            }
        }
    }

    fun loadMorePhotos(context: Context) {
        if (isLoadingMore) return
        isLoadingMore = true

        viewModelScope.launch(Dispatchers.IO) {
            if (PermissionUtil.hasReadExternalPermission(context)) {
                val newPhotos = VslImageHandlerUtil.queryPhotoChunkManualIo(
                    context,
                    offset = currentOffset,
                    limit = batchSize,
                    preloadWidth = 130.pxToDp().value.toInt(),
                    preloadHeight = 130.pxToDp().value.toInt()
                )
                if (newPhotos.isNotEmpty()) {
                    currentOffset += newPhotos.size
                    _uiState.update { state ->
                        state.copy(
                            photos = state.photos.orEmpty() + newPhotos
                        )
                    }
                }
            }
            isLoadingMore = false
        }
    }
}

internal data class VslPickPhotoUiState(
    val photos: List<PhotoItem>? = null,
    val selectedPhoto: PhotoItem? = null,
    val isNextEnabled: Boolean = false
)

internal sealed class VslPickPhotoUiEffect {
    object BackNavigation : VslPickPhotoUiEffect()
    data class NextNavigation(val photo: PhotoItem) : VslPickPhotoUiEffect()
}

internal sealed class VslPickPhotoEvent {
    object OnBackClicked : VslPickPhotoEvent()
    object OnNextClicked : VslPickPhotoEvent()
    data class OnPhotoSelected(val photo: PhotoItem) : VslPickPhotoEvent()
}