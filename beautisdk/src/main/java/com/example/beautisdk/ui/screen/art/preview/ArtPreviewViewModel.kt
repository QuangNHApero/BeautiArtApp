package com.example.beautisdk.ui.screen.art.preview

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautisdk.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class ArtPreviewViewModel : ViewModel() {
    var uiState by mutableStateOf(GenerateArtUiState())
        private set

    private val _effect = MutableSharedFlow<GenerateArtUiEffect>()
    val effect: SharedFlow<GenerateArtUiEffect> = _effect.asSharedFlow()

    fun onEvent(event: GenerateArtUiEvent) {
        when (event) {
            is GenerateArtUiEvent.OnPromptChanged -> {
                uiState = uiState.copy(prompt = event.prompt)
                validateGenerateButton()
            }
            is GenerateArtUiEvent.OnPhotoSelected -> {
                uiState = uiState.copy(photoUri = event.uri)
                validateGenerateButton()
            }
            is GenerateArtUiEvent.OnStyleSelected -> {
                uiState = uiState.copy(selectedStyleId = event.styleId)
                validateGenerateButton()
            }
            is GenerateArtUiEvent.OnGenerateClicked -> {
                generateImage()
            }
        }
    }

    private fun validateGenerateButton() {
        uiState = uiState.copy(
            isGenerateButtonEnabled = (!uiState.prompt.isBlank() || uiState.selectedStyleId != null) && uiState.photoUri != null
        )
    }

    fun updatePhotoUri(uri: Uri) {
        uiState = uiState.copy(photoUri = uri)
        validateGenerateButton()
    }

    private fun generateImage() {
        viewModelScope.launch {
            _effect.emit(GenerateArtUiEffect.ShowLoading)

            delay(2000) // giả lập network

            val success = (0..1).random() == 1

            _effect.emit(GenerateArtUiEffect.HideLoading)
            if (!success) {
                _effect.emit(GenerateArtUiEffect.ShowError(R.string.snackbar_error_network))
            } else {
                uiState = uiState.copy(photoUri = null)
                _effect.emit(GenerateArtUiEffect.Success)
            }
        }
    }
}

data class GenerateArtUiState(
    val prompt: String = "",
    val photoUri: Uri? = null,
    val selectedStyleId: String? = null,
    val isGenerateButtonEnabled: Boolean = false
)

sealed class GenerateArtUiEffect {
    object ShowLoading : GenerateArtUiEffect()
    object HideLoading : GenerateArtUiEffect()
    data class ShowError(val message: Int) : GenerateArtUiEffect()
    object Success : GenerateArtUiEffect()
}

sealed class GenerateArtUiEvent {
    data class OnPromptChanged(val prompt: String) : GenerateArtUiEvent()
    data class OnPhotoSelected(val uri: Uri) : GenerateArtUiEvent()
    data class OnStyleSelected(val styleId: String) : GenerateArtUiEvent()
    object OnGenerateClicked : GenerateArtUiEvent()
}
