package com.example.beautisdk.ui.screen.result

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beautisdk.R
import com.example.beautisdk.utils.VslImageHandlerUtil
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class VslResultViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    private val _effect = Channel<ResultUiEffect>()
    val effect = _effect.receiveAsFlow()

    fun setImageUri(uri: Uri) {
        _uiState.update { it.copy(photoUri = uri) }
    }

    fun onEvent(event: ResultUiEvent) {
        viewModelScope.launch {
            when (event) {
                is ResultUiEvent.OnBackClicked -> {
                    _effect.send(ResultUiEffect.BackNavigation)
                }
                is ResultUiEvent.OnDownloadClicked -> {
                    _effect.send(ResultUiEffect.RequestWritePermission)
                }
            }
        }
    }

    fun downloadImageAfterPermissionGranted(context: Context) {
        val uri = uiState.value.photoUri ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(showLoadingDialog = true) }

            val result = VslImageHandlerUtil.saveImageToExternal(context, uri)

            _uiState.update { it.copy(showLoadingDialog = false) }

            if (result.isSuccess) {
                _effect.send(ResultUiEffect.ShowSnackbar(R.string.txt_success_download, true))
            } else {
                _effect.send(ResultUiEffect.ShowSnackbar(R.string.txt_failed_download, false))
            }
        }
    }
}

internal data class ResultUiState(
    val photoUri: Uri? = null,
    val showLoadingDialog: Boolean = false
)

internal sealed class ResultUiEffect {
    object BackNavigation : ResultUiEffect()
    object RequestWritePermission : ResultUiEffect()
    data class ShowSnackbar(val messageResId: Int, val isSuccess: Boolean) : ResultUiEffect()
}

internal sealed class ResultUiEvent {
    object OnBackClicked : ResultUiEvent()
    object OnDownloadClicked : ResultUiEvent()
}
