package com.example.beautisdk.ui.screen.result

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.lifecycle.lifecycleScope
import com.example.beautisdk.R
import com.example.beautisdk.base.BaseActivity
import com.example.beautisdk.ui.component.CustomGradientButton
import com.example.beautisdk.ui.component.CustomSnackbar
import com.example.beautisdk.ui.component.LoadingDialog
import com.example.beautisdk.ui.component.PreviewImageCard
import com.example.beautisdk.ui.design_system.LocalCustomTypography
import com.example.beautisdk.ui.design_system.component.AperoTextView
import com.example.beautisdk.ui.design_system.pxToDp
import com.example.beautisdk.utils.VslImageHandlerUtil
import com.example.beautisdk.utils.PermissionUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal class VslResultActivity : BaseActivity() {
    private val _effect = MutableSharedFlow<ResultUiEffect>()

    private val uri: Uri? by lazy { intent.data }
    override fun onBackNavigation() {
        finish()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            downloadImage()
        }
    }

    @Composable
    override fun UpdateUI(modifier: Modifier) {
        var showLoadingDialog by remember { mutableStateOf(false) }
        var showSnackbar by remember { mutableStateOf(false) }
        var message by remember { mutableStateOf(R.string.snackbar_error_network) }
        var isSuccess by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            _effect.collectLatest { effect ->
                when (effect) {
                    is ResultUiEffect.ShowLoading -> {
                        showLoadingDialog = true
                    }

                    is ResultUiEffect.ShowSuccess,
                    is ResultUiEffect.ShowError -> {
                        showLoadingDialog = false
                        val msg = when (effect) {
                            is ResultUiEffect.ShowSuccess -> {
                                isSuccess = true
                                effect.message
                            }

                            is ResultUiEffect.ShowError -> {
                                isSuccess = false
                                effect.message
                            }

                            else -> R.string.snackbar_error_network
                        }
                        message = msg
                        showSnackbar = true
                        delay(3000)
                        showSnackbar = false
                    }
                }
            }
        }

        Box(modifier = modifier.fillMaxSize()) {
            MainContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.systemBars.asPaddingValues())
            )
            if (showLoadingDialog) {
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
        uri: Uri? = this.uri,
        @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
    ) {
        val context = LocalContext.current
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
                    .clickable { onBackNavigation() }
            )


            PreviewImageCard(
                imageUri = uri,
                isResult = true,
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { }
            )


            CustomGradientButton(
                isEnabled = uri != null,
                onClick = {
                    PermissionUtil.checkAndRequestWritePermission(
                        context,
                        requestPermissionLauncher
                    )
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

    private fun downloadImage() {
        uri?.let { imageUri ->
            lifecycleScope.launch {
                _effect.emit(ResultUiEffect.ShowLoading)

                VslImageHandlerUtil.saveImageToExternal(
                    context = this@VslResultActivity,
                    uri = imageUri,
                    onSuccess = {
                        _effect.tryEmit(ResultUiEffect.ShowSuccess(R.string.txt_success_download))
                    },
                    onError = {
                        _effect.tryEmit(ResultUiEffect.ShowError(R.string.txt_failed_download))
                    }
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

internal sealed class ResultUiEffect {
    object ShowLoading : ResultUiEffect()
    data class ShowSuccess(val message: Int) : ResultUiEffect()
    data class ShowError(val message: Int) : ResultUiEffect()
}
