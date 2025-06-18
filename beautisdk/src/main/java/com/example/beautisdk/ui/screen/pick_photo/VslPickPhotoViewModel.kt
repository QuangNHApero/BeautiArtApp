package com.example.beautisdk.ui.screen.pick_photo

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautisdk.ui.screen.pick_photo.data.PhotoItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

internal class VslPickPhotoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(VslPickPhotoUiState())
    val uiState: StateFlow<VslPickPhotoUiState> = _uiState.asStateFlow()

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
}

internal data class VslPickPhotoUiState(
    val photos: List<PhotoItem>? = fakePhotos,
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

val thumbnailUrls = listOf(
    "https://static.apero.vn/video-editor-pro/ai-style-thumbnail/Neon_City_Before.jpg",
    "https://static.apero.vn/video-editor-pro/ai-style-thumbnail/Neon_City_After.jpg",
    "https://static.apero.vn/video-editor-pro/ai-style-thumbnail/1701157351-image.png",
    "https://static.apero.vn/video-editor-pro/ai-style-thumbnail/1701402945-image.png",
    "https://static.apero.vn/video-editor-pro/ai-style-thumbnail/1702023264-image.png",
    "https://static.apero.vn/video-editor-pro/ai-style-thumbnail/1702023277-image.png",
    "https://static.apero.vn/video-editor-pro/ai-style-thumbnail/1702026727-image.png",
    "https://static.apero.vn/video-editor-pro/ai-style-thumbnail/Ackerman_Girl_Before.jpg",
    "https://static.apero.vn/video-editor-pro/ai-style-thumbnail/Ackerman_Girl_After.jpg",
    "https://static.apero.vn/video-editor-pro/ai-style-thumbnail/1702020921-image.png",
    "https://static.apero.vn/video-editor-pro/ai-style-thumbnail/1702875462300-Waterart_After.jpg",
    "https://static.apero.vn/video-editor-pro/ai-style-thumbnail/1707278449098-Race%2520man_After.jpg"
)

val fakePhotos = thumbnailUrls.mapIndexed { index, url ->
    PhotoItem(
        id = index,
        uri = Uri.parse(url)
    )
}