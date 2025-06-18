package com.example.beautisdk.ui.screen.art.preview

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautisdk.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class VslArtPreviewViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GenerateArtUiState())
    val uiState: StateFlow<GenerateArtUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<GenerateArtUiEffect>()
    val effect: SharedFlow<GenerateArtUiEffect> = _effect.asSharedFlow()

    fun onEvent(event: GenerateArtUiEvent) {
        when (event) {
            is GenerateArtUiEvent.OnPromptChanged -> {
                _uiState.value = _uiState.value.copy(prompt = event.prompt)
                validateGenerateButton()
            }
            is GenerateArtUiEvent.OnPhotoSelected -> {
                _uiState.value = _uiState.value.copy(photoUri = event.uri)
                validateGenerateButton()
            }
            is GenerateArtUiEvent.OnStyleSelected -> {
                _uiState.value = _uiState.value.copy(selectedStyleId = event.styleId)
                validateGenerateButton()
            }
            is GenerateArtUiEvent.OnGenerateClicked -> {
                generateImage()
            }
        }
    }

    private fun validateGenerateButton() {
        _uiState.value = _uiState.value.copy(
            isGenerateButtonEnabled = (!_uiState.value.prompt.isBlank() || uiState.value.selectedStyleId != null) && uiState.value.photoUri != null
        )
    }

    fun updatePhotoUri(uri: Uri) {
        _uiState.value = _uiState.value.copy(photoUri = uri)
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
                val resultUri = Uri.parse("TODO: generate uri")
                _uiState.value = _uiState.value.copy(photoUri = resultUri)
                _effect.emit(GenerateArtUiEffect.Success(resultUri))
            }
        }
    }
}

internal data class GenerateArtUiState(
    val prompt: String = "",
    val photoUri: Uri? = null,
    val selectedStyleId: String? = null,
    val isGenerateButtonEnabled: Boolean = false
)

internal sealed class GenerateArtUiEffect {
    object ShowLoading : GenerateArtUiEffect()
    object HideLoading : GenerateArtUiEffect()
    data class ShowError(val message: Int) : GenerateArtUiEffect()
    data class Success(val generatedPhoto: Uri) : GenerateArtUiEffect()
}

internal sealed class GenerateArtUiEvent {
    data class OnPromptChanged(val prompt: String) : GenerateArtUiEvent()
    data class OnPhotoSelected(val uri: Uri) : GenerateArtUiEvent()
    data class OnStyleSelected(val styleId: String) : GenerateArtUiEvent()
    object OnGenerateClicked : GenerateArtUiEvent()
}
