package com.example.beautisdk.ui.screen.art.preview

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aperoaiservice.domain.model.CategoryArt
import com.example.aperoaiservice.domain.model.StyleArt
import com.example.aperoaiservice.network.model.AiArtParams
import com.example.aperoaiservice.network.repository.AiArtRepository
import com.example.aperoaiservice.network.response.ResponseState
import com.example.artbeautify.utils.ext.isNetworkAvailable
import com.example.beautisdk.R
import com.example.beautisdk.data.VslBeautiRemote
import com.example.beautisdk.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

internal class VslArtPreviewViewModel(
    private val aiServiceRepository: AiArtRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GenerateArtUiState(
            categories = VslBeautiRemote.remoteCategorys
        )
    )
    val uiState: StateFlow<GenerateArtUiState> = _uiState.asStateFlow()

    private var selectedStyle: StyleArt? = null

    private val _effect = Channel<GenerateArtUiEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: GenerateArtUiEvent) {
        viewModelScope.launch {
            when (event) {
                is GenerateArtUiEvent.OnPromptChanged -> {
                    _uiState.update { it.copy(prompt = event.prompt) }
                    validateGenerateButton()
                }

                is GenerateArtUiEvent.OnPhotoSelected -> {
                    _uiState.update { it.copy(photoUri = event.uri) }
                    validateGenerateButton()
                }

                is GenerateArtUiEvent.OnStyleSelected -> {
                    val style = withContext(Dispatchers.Default) {
                        _uiState.value.categories
                            .getOrNull(_uiState.value.selectedCategoryIndex)
                            ?.styles
                            ?.firstOrNull { it._id == event.styleId }
                    }
                    selectedStyle = style
                    _uiState.update { it.copy(selectedStyleId = event.styleId) }
                    validateGenerateButton()
                }

                is GenerateArtUiEvent.OnGenerateClicked -> {
                    generateImage(event.context)
                }

                is GenerateArtUiEvent.OnChoosePhotoClicked -> {
                    _effect.send(GenerateArtUiEffect.NavigationPhotoPicker)
                }

                is GenerateArtUiEvent.OnCategorySelected -> {
                    onCategorySelected(event.categoryIndex)
                }
            }
        }
    }

    private fun validateGenerateButton() {
        _uiState.update {
            it.copy(
                isGenerateButtonEnabled =
                (
                        !_uiState.value.prompt.isBlank() ||
                                uiState.value.selectedStyleId != null
                        ) &&
                        uiState.value.photoUri != null
            )
        }
    }

    fun onCategorySelected(index: Int) {
        _uiState.update {
            it.copy(
                selectedCategoryIndex = index,
                selectedStyleId = null
            )
        }
    }

    fun updatePhotoUri(uri: Uri) {
        _uiState.update { it.copy(photoUri = uri) }
        validateGenerateButton()
    }

    private fun generateImage(context: Context) {
        viewModelScope.launch {
            if (!context.isNetworkAvailable()) {
                _effect.send(GenerateArtUiEffect.ShowError(R.string.snackbar_error_network))
                return@launch
            }
            _effect.send(GenerateArtUiEffect.ShowLoading)

            if (_uiState.value.photoUri == null) return@launch

            val result = FileUtils.copyUriToCacheFile(context, _uiState.value.photoUri!!)

            result.onSuccess { file ->
                val realPath = file.absolutePath
                val prompt = _uiState.value.prompt.ifEmpty { selectedStyle?.positivePrompt.orEmpty() }
                val modeInt = selectedStyle?.mode?.toIntOrNull() ?: 0

                val responseResult = aiServiceRepository.genArtAi(
                    AiArtParams(
                        pathImageOrigin = realPath,
                        styleId = _uiState.value.selectedStyleId,
                        positivePrompt = prompt,
                        negativePrompt = selectedStyle?.negativePrompt,
                        baseModel = selectedStyle?.basemodel,
                        mode = modeInt
                    )
                )

                _effect.send(GenerateArtUiEffect.HideLoading)

                when (responseResult) {
                    is ResponseState.Success -> {
                        val path = responseResult.data?.path
                        val resultUri = path?.let { File(it).toUri() }

                        if (resultUri != null){
                            _uiState.value = _uiState.value.copy(photoUri = resultUri)
                            _effect.send(GenerateArtUiEffect.Success(resultUri))
                        } else {
                            _effect.send(GenerateArtUiEffect.ShowError(R.string.snackbar_error_network))

                        }
                    }
                    is ResponseState.Error -> {
                        Log.d("TAG", "generateImage: ${responseResult.error}")
                        Log.d("TAG", "generateImage: ${responseResult.code}")

                        _effect.send(GenerateArtUiEffect.ShowError(R.string.snackbar_error_network))
                    }
                }
            }.onFailure { error ->
                return@launch
            }
        }
    }
}

internal data class GenerateArtUiState(
    val prompt: String = "",
    val photoUri: Uri? = null,
    val selectedStyleId: String? = null,
    val selectedCategoryIndex: Int = 0,
    val isGenerateButtonEnabled: Boolean = false,
    val categories: List<CategoryArt> = listOf()
)

internal sealed class GenerateArtUiEffect {
    object ShowLoading : GenerateArtUiEffect()
    object HideLoading : GenerateArtUiEffect()
    object NavigationPhotoPicker : GenerateArtUiEffect()
    data class ShowError(val message: Int) : GenerateArtUiEffect()
    data class Success(val generatedPhoto: Uri) : GenerateArtUiEffect()
}

internal sealed class GenerateArtUiEvent {
    data class OnPromptChanged(val prompt: String) : GenerateArtUiEvent()
    data class OnPhotoSelected(val uri: Uri) : GenerateArtUiEvent()
    data class OnStyleSelected(val styleId: String) : GenerateArtUiEvent()
    data class OnCategorySelected(val categoryIndex: Int) : GenerateArtUiEvent()
    data class OnGenerateClicked(val context: Context) : GenerateArtUiEvent()
    object OnChoosePhotoClicked : GenerateArtUiEvent()
}
