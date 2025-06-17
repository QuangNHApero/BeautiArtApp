package com.example.beautisdk.ui.screen.art.preview

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.aperoaiservice.art.repository.StyleRepository
import com.example.beautisdk.R
import com.example.beautisdk.base.BaseActivityPreview
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ArtPreviewActivity : BaseActivityPreview() {
    private val viewModel: ArtPreviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiUrl = "https://api-style-manager.apero.vn/category?project=techtrek&segmentValue=IN&styleType=imageToImage&isApp=true"

        lifecycleScope.launch {
            val result = StyleRepository.getStyleArts(apiUrl)
            val top10 = result.flatMap { it.styles }.take(10)

            top10.forEachIndexed { index, style ->
                Log.d("STYLE_ART", "[$index] ${style.name} - ${style.thumbnail}")
            }
        }
    }

    override fun onBackNavigation() {
        finish()
    }

    override fun onImagePicked(uri: Uri) {
        viewModel.updatePhotoUri(uri)
    }

    @Composable
    override fun UpdateUI(modifier: Modifier) {
        val uiState by viewModel.uiState.collectAsState()

        var showLoadingDialog by remember { mutableStateOf(false) }
        var showErrorSnackbar by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf(R.string.snackbar_error_network) }
        LaunchedEffect(Unit) {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    is GenerateArtUiEffect.ShowLoading -> {
                        // handle loading overlay
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
                        VslResultActivity.start(this@ArtPreviewActivity, effect.generatedPhoto)
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
                onChoosePhoto = { launchCustomPickPhoto() },
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
fun MainContent(
    state: GenerateArtUiState,
    onEvent: (GenerateArtUiEvent) -> Unit,
    onChoosePhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(
            top = 35.pxToDp(),
            bottom = 80.pxToDp(),
            start = 25.pxToDp()
        )
    ) {
        PromptCard(
            text = state.prompt,
            onValueChange = { onEvent(GenerateArtUiEvent.OnPromptChanged(it)) },
            onClose = { onEvent(GenerateArtUiEvent.OnPromptChanged("")) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 25.pxToDp())
        )

        Spacer(modifier = Modifier.height(29.pxToDp()))

        PreviewImageCard(
            imageUri = state.photoUri,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 25.pxToDp()),
            onClick = { onChoosePhoto() }
        )

        Spacer(modifier = Modifier.height(29.pxToDp()))

        AperoTextView(
            text = stringResource(R.string.txt_choose_style),
            textStyle = LocalCustomTypography.current.Title3.bold.copy(color = Color(0xFFE400D9)),
            maxLines = 1,
            marqueeEnabled = true,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(5.pxToDp()))



        Spacer(modifier = Modifier.height(30.pxToDp()))

        CustomGradientButton(
            isEnabled = state.isGenerateButtonEnabled,
            onClick = { onEvent(GenerateArtUiEvent.OnGenerateClicked) },
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