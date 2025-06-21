package com.example.beautisdk.ui.screen.art.preview

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aperoaiservice.network.model.AiArtParams
import com.example.aperoaiservice.network.repository.AiArtRepository
import com.example.aperoaiservice.network.response.ResponseState
import com.example.artbeautify.utils.ext.isNetworkAvailable
import com.example.beautisdk.R
import com.example.beautisdk.ui.data.model.CategoryArt
import com.example.beautisdk.ui.data.model.StyleArt
import com.example.beautisdk.utils.FileUtils
import com.example.beautisdk.utils.repository.ArtRepository
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
import java.util.concurrent.atomic.AtomicBoolean

internal class VslArtPreviewViewModel(
    private val aiServiceRepository: AiArtRepository,
    private val dataRepository: ArtRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GenerateArtUiState(
            categories = skeletonCategories,
        )
    )
    val uiState: StateFlow<GenerateArtUiState> = _uiState.asStateFlow()

    private var isFetchingCategories = AtomicBoolean(false)
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

                is GenerateArtUiEvent.OnRefreshCategories -> {
                    if (!isFetchingCategories.get() && _uiState.value.categories == skeletonCategories) {
                        fetchCategories()
                    }
                }
            }
        }
    }

    private suspend fun fetchCategories() {
        isFetchingCategories.set(true)
        dataRepository.loadFromRemote().collect { categories ->
            if (categories.isNotEmpty()) {
                _uiState.update {
                    it.copy(categories = categories)
                }
            }
        }
        isFetchingCategories.set(false)
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
                val prompt =
                    _uiState.value.prompt.ifEmpty { selectedStyle?.positivePrompt.orEmpty() }
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

                        if (resultUri != null) {
                            _uiState.value = _uiState.value.copy(photoUri = resultUri)
                            _effect.send(GenerateArtUiEffect.Success(resultUri))
                        } else {
                            _effect.send(GenerateArtUiEffect.ShowError(R.string.snackbar_error_network))

                        }
                    }

                    is ResponseState.Error -> {

                        val code = responseResult.code

                        val errorResId = when (code) {
                            401 -> R.string.snackbar_error_unauthorized
                            403 -> R.string.snackbar_error_forbidden
                            404 -> R.string.snackbar_error_not_found
                            500 -> R.string.snackbar_error_server
                            else -> R.string.snackbar_error_generic
                        }

                        _effect.send(GenerateArtUiEffect.ShowError(errorResId))
                    }
                }
            }.onFailure { error ->
                return@launch
            }
        }
    }
}

// Tạo 6 danh mục giả, mỗi danh mục chứa 5 style giả
internal val skeletonCategories: List<CategoryArt> = List(3) { catIndex ->

    // 5 style placeholder cho 1 danh mục
    val stylePlaceholders = List(5) { styleIndex ->
        StyleArt(
            _id = "placeholder-${catIndex}_$styleIndex",
            name = "need internet",
            thumbnail = "",
            positivePrompt = null,
            negativePrompt = null,
            mode = null,
            basemodel = null
        )
    }

    CategoryArt(
        _id = "placeholder-$catIndex",
        name = "need internet",
        styles = stylePlaceholders
    )
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
    object OnRefreshCategories : GenerateArtUiEvent()
}
