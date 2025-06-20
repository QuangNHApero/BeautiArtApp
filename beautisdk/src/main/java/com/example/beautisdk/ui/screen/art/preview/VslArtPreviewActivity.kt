package com.example.beautisdk.ui.screen.art.preview

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import com.example.artbeautify.utils.ext.isNetworkAvailable
import com.example.beautisdk.R
import com.example.beautisdk.base.BaseActivityPreview
import com.example.beautisdk.ui.component.ChooseStyleScreen
import com.example.beautisdk.ui.component.CustomGradientButton
import com.example.beautisdk.ui.component.CustomSnackbar
import com.example.beautisdk.ui.component.LoadingDialog
import com.example.beautisdk.ui.component.PreviewImageCard
import com.example.beautisdk.ui.component.PromptCard
import com.example.beautisdk.ui.design_system.AppTheme
import com.example.beautisdk.ui.design_system.LocalCustomTypography
import com.example.beautisdk.ui.design_system.component.AperoTextView
import com.example.beautisdk.ui.design_system.pxToDp
import com.example.beautisdk.ui.screen.result.VslResultActivity
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel


internal class VslArtPreviewActivity : BaseActivityPreview() {
    private val viewModel: VslArtPreviewViewModel by viewModel()

    override fun onBackNavigation() {
        finish()
    }

    override fun onImagePicked(uri: Uri) {
        viewModel.updatePhotoUri(uri)
    }

    @Composable
    override fun UpdateUI(modifier: Modifier) {
        val uiState by viewModel.uiState.collectAsState()
        val context = LocalContext.current

        var showLoadingDialog by remember { mutableStateOf(false) }
        var showErrorSnackbar by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf(R.string.snackbar_error_network) }

        ObserveLifecycle { event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    if (context.isNetworkAvailable()) {
                        viewModel.onEvent(GenerateArtUiEvent.OnRefreshCategories)
                    }
                }

                else -> Unit
            }
        }

        ObserveWindowFocus { hasFocus ->
            if (hasFocus && context.isNetworkAvailable()) {
                viewModel.onEvent(GenerateArtUiEvent.OnRefreshCategories)
            }
        }


        LaunchedEffect(Unit) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is GenerateArtUiEffect.ShowLoading -> {
                        showLoadingDialog = true
                    }

                    is GenerateArtUiEffect.HideLoading -> {
                        showLoadingDialog = false
                    }

                    is GenerateArtUiEffect.ShowError -> {
                        showErrorSnackbar = true
                        errorMessage = effect.message
                        delay(3000)
                        showErrorSnackbar = false
                    }

                    is GenerateArtUiEffect.Success -> {
                        VslResultActivity.start(this@VslArtPreviewActivity, effect.generatedPhoto)
                    }

                    is GenerateArtUiEffect.NavigationPhotoPicker -> {
                        checkAndLaunchPickPhoto()
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            MainContent(
                state = uiState,
                onEvent = viewModel::onEvent,
                focusTxtColor = config.focusTxtColor,
                focusbgColor = config.focusBgColor,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.systemBars.asPaddingValues())
            )
            if (showLoadingDialog) {
                LoadingDialog()
            }
            if (showErrorSnackbar) {
                CustomSnackbar(
                    textResId = errorMessage,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(WindowInsets.systemBars.asPaddingValues())
                )
            }
        }
    }
}

@Composable
internal fun MainContent(
    state: GenerateArtUiState,
    focusTxtColor: Color,
    focusbgColor: Color,
    onEvent: (GenerateArtUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.padding(
            top = 35.pxToDp(),
            bottom = 80.pxToDp(),
            start = 25.pxToDp()
        ),
        verticalArrangement = Arrangement.spacedBy(25.pxToDp())
    ) {
        PromptCard(
            text = state.prompt,
            onValueChange = { onEvent(GenerateArtUiEvent.OnPromptChanged(it)) },
            onClose = { onEvent(GenerateArtUiEvent.OnPromptChanged("")) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 25.pxToDp())
        )

        PreviewImageCard(
            imageUri = state.photoUri,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 25.pxToDp()),
            onClick = { onEvent(GenerateArtUiEvent.OnChoosePhotoClicked) }
        )

        ChooseStyleScreen(
            uiState = state,
            onCategorySelected = { onEvent(GenerateArtUiEvent.OnCategorySelected(it)) },
            onStyleSelected = { onEvent(GenerateArtUiEvent.OnStyleSelected(it)) },
            selectedColor = focusTxtColor,
            unselectedColor = Color.Black,
            selectedBackgroundColor = focusbgColor,
            modifier = Modifier.fillMaxWidth()
        )

        CustomGradientButton(
            isEnabled = state.isGenerateButtonEnabled,
            onClick = { onEvent(GenerateArtUiEvent.OnGenerateClicked(context)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 25.pxToDp())
        ) {
            AperoTextView(
                text = stringResource(R.string.btn_generate),
                textStyle = LocalCustomTypography.current.Body.bold.copy(color = Color.White),
                textAlign = TextAlign.Center,
                maxLines = 1,
                marqueeEnabled = true
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
    }
}