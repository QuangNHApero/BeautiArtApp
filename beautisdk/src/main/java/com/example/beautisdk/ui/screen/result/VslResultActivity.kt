package com.example.beautisdk.ui.screen.result

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.beautisdk.R
import com.example.beautisdk.base.BaseActivity
import com.example.beautisdk.ui.component.CustomGradientButton
import com.example.beautisdk.ui.component.CustomSnackbar
import com.example.beautisdk.ui.component.LoadingDialog
import com.example.beautisdk.ui.component.PreviewImageCard
import com.example.beautisdk.ui.design_system.LocalCustomTypography
import com.example.beautisdk.ui.design_system.component.AperoTextView
import com.example.beautisdk.ui.design_system.pxToDp
import com.example.beautisdk.utils.PermissionUtil
import kotlinx.coroutines.delay

internal class VslResultActivity : BaseActivity() {
    private val viewModel: VslResultViewModel by viewModels()
    override fun onBackNavigation() {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent?.data
        if (uri != null) {
            viewModel.setImageUri(uri)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.downloadImageAfterPermissionGranted(this)
        }
    }

    @Composable
    override fun UpdateUI(modifier: Modifier) {
        val uiState by viewModel.uiState.collectAsState()
        val context = LocalContext.current

        var showSnackbar by remember { mutableStateOf(false) }
        var message by remember { mutableStateOf(R.string.snackbar_error_network) }
        var isSuccess by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is ResultUiEffect.BackNavigation -> {
                        onBackNavigation()
                    }
                    is ResultUiEffect.RequestWritePermission -> {
                        PermissionUtil.checkAndRequestWritePermission(
                            context,
                            requestPermissionLauncher,
                            onGranted = { viewModel.downloadImageAfterPermissionGranted(context) }
                        )
                    }
                    is ResultUiEffect.ShowSnackbar -> {
                        showSnackbar = true
                        message = effect.messageResId
                        isSuccess = effect.isSuccess
                        delay(3000)
                        showSnackbar = false
                    }
                }
            }
        }

        Box(modifier = modifier.fillMaxSize()) {
            MainContent(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.systemBars.asPaddingValues())
            )
            if (uiState.showLoadingDialog) {
                LoadingDialog(
                    loadingResId = config.uiConfig.loadingRawId
                )
            }
            if (showSnackbar) {
                CustomSnackbar(
                    textResId = message,
                    isError = !isSuccess,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(WindowInsets.systemBars.asPaddingValues())
                )
            }
        }
    }

    @Composable
    fun MainContent(
        uiState: ResultUiState,
        onEvent: (ResultUiEvent) -> Unit,
        @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier.padding(
                top = 35.pxToDp(),
                bottom = 80.pxToDp(),
                start = 25.pxToDp(),
                end = 25.pxToDp()
            ),
            verticalArrangement = Arrangement.spacedBy(150.pxToDp()),
        ) {
            Image(
                painter = painterResource(id = config.backBtnResId),
                contentDescription = "ic_back",
                modifier = Modifier
                    .clickable { onEvent(ResultUiEvent.OnBackClicked) }
            )


            PreviewImageCard(
                imageUri = uiState.photoUri,
                isResult = true,
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { }
            )


            CustomGradientButton(
                isEnabled = uiState.photoUri != null,
                onClick = {
                    onEvent(ResultUiEvent.OnDownloadClicked)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 25.pxToDp())
            ) {
                AperoTextView(
                    text = stringResource(R.string.btn_download),
                    textStyle = LocalCustomTypography.current.Body.bold.copy(color = Color.White),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    marqueeEnabled = true
                )
            }
        }
    }

    companion object {
        fun start(context: Context, uri: Uri) {
            val intent = Intent(context, VslResultActivity::class.java).apply {
                data = uri
            }
            context.startActivity(intent)
        }
    }
}


