package com.example.beautisdk.ui.screen.pick_photo

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.beautisdk.R
import com.example.beautisdk.base.BaseActivity
import com.example.beautisdk.ui.design_system.LocalCustomTypography
import com.example.beautisdk.ui.design_system.component.AperoTextView
import com.example.beautisdk.ui.design_system.pxToDp
import com.example.beautisdk.ui.screen.pick_photo.component.PhotoGallery
import kotlinx.coroutines.flow.distinctUntilChanged

internal class VslPickPhotoActivity : BaseActivity() {
    private val viewModel: VslPickPhotoViewModel by viewModels()
    override fun onBackNavigation() {
        finish()
    }

    @Composable
    override fun UpdateUI(modifier: Modifier) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            if (uiState.photos.isNullOrEmpty()) {
                viewModel.loadMorePhotos(context)
            }
        }


        LaunchedEffect(Unit) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is VslPickPhotoUiEffect.BackNavigation -> {
                        onBackNavigation()
                    }
                    is VslPickPhotoUiEffect.NextNavigation -> {
                        uiState.selectedPhoto?.let { onPhotoApply(it.uri) }
                    }
                }
            }
        }

        MainContent(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            modifier = modifier
                .fillMaxSize()
                .padding(WindowInsets.systemBars.asPaddingValues())
        )
    }

    @Composable
    fun MainContent(
        uiState: VslPickPhotoUiState,
        onEvent: (VslPickPhotoEvent) -> Unit,
        @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
    ) {
        val context = LocalContext.current
        val gridState = rememberLazyGridState()

        LaunchedEffect(gridState) {
            snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .distinctUntilChanged()
                .collect { lastVisibleIndex ->
                    val totalItems = gridState.layoutInfo.totalItemsCount
                    if (lastVisibleIndex != null && lastVisibleIndex >= totalItems - 30 && totalItems > 0) {
                        viewModel.loadMorePhotos(context)
                    }
                }
        }


        Column(
            modifier = modifier
                .padding(horizontal = 22.pxToDp())
        ) {
            Spacer(modifier = Modifier.height(16.pxToDp()))

            HeaderBar(uiState, onEvent, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(36.pxToDp()))

            PhotoGallery(
                photoList = uiState.photos,
                selectedPhotoId = uiState.selectedPhoto?.id,
                onPhotoClick = { photo -> onEvent(VslPickPhotoEvent.OnPhotoSelected(photo)) },
                modifier = Modifier.weight(1f),
                selectedResId = config.selectedBtn,
                unselectedResId = config.unSelectedBtn,
                gridState = gridState,
            )
        }
    }

    @Composable
    fun HeaderBar(
        uiState: VslPickPhotoUiState,
        onEvent: (VslPickPhotoEvent) -> Unit,
        modifier: Modifier = Modifier
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = config.closeBtnResId),
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.pxToDp())
                    .clickable { onEvent(VslPickPhotoEvent.OnBackClicked) }
            )


            AperoTextView(
                text = getString(R.string.btn_next),
                textStyle = LocalCustomTypography.current.Title3.bold.copy(
                    color = if (uiState.isNextEnabled) Color(0xFF131318) else Color(0x80131318)
                ),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .clickable(enabled = uiState.isNextEnabled) { onEvent(VslPickPhotoEvent.OnNextClicked) }
            )
        }
    }

    private fun onPhotoApply(selectedUri: Uri) {
        val intent = Intent().apply {
            data = selectedUri
        }
        setResult(RESULT_OK, intent)
        finish()
    }

}