package com.example.beautisdk.ui.screen.pick_photo.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.beautisdk.R
import com.example.beautisdk.ui.design_system.LocalCustomTypography
import com.example.beautisdk.ui.design_system.component.AperoTextView
import com.example.beautisdk.ui.design_system.pxToDp
import com.example.beautisdk.ui.screen.pick_photo.data.PhotoItem

@Composable
fun PhotoGallery(
    photoList: List<PhotoItem>?,
    selectedPhotoId: Int?,
    onPhotoClick: (PhotoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()

    if (photoList.isNullOrEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_photo),
                contentDescription = "choose image",
                modifier = Modifier.size(150.pxToDp())
            )
            Spacer(modifier = Modifier.height(16.pxToDp()))
            AperoTextView(
                text = stringResource(R.string.txt_pickphoto_empty),
                textStyle = LocalCustomTypography.current.LargeTitle.bold.copy(color = Color.Black),
                textAlign = TextAlign.Center,
                maxLines = 1,
                marqueeEnabled = true
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = gridState,
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.pxToDp()),
            horizontalArrangement = Arrangement.spacedBy(10.pxToDp())
        ) {
            items(items = photoList, key = { it.id }) { item ->
                val isSelected = item.id == selectedPhotoId
                PhotoItem(
                    photoUri = item.uri,
                    isSelected = isSelected,
                    onPhotoClick = { onPhotoClick(item) }
                )
            }
        }
    }
}

@Composable
fun PhotoItem(photoUri: Uri, onPhotoClick: (Uri) -> Unit, isSelected: Boolean = false) {
    val overlayIcon by remember(isSelected) {
        mutableStateOf(
            if (isSelected) R.drawable.ic_pickphoto_selected
            else R.drawable.ic_pickphoto_unselected
        )
    }

    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(photoUri)
            .placeholder(R.drawable.ic_photo)
            .crossfade(true)
            .build()
    )


    Box(
        modifier = Modifier
            .size(130.pxToDp())
            .clip(RoundedCornerShape(10.pxToDp()))
            .clickable { onPhotoClick(photoUri)}
    ) {
        Image(
            painter = painter,
            contentDescription = "Photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize(),
        )


        Image(
            painter = painterResource(id = overlayIcon),
            contentDescription = "Select state",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.pxToDp())
                .size(24.pxToDp())
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPhotoGalleryEmpty() {
    val fakePhotos = emptyList<PhotoItem>()

    PhotoGallery(
        photoList = fakePhotos,
        selectedPhotoId = null,
        onPhotoClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPhotoGallery() {
    val fakePhotos = List(12) { index ->
        PhotoItem(
            id = index,
            uri = Uri.parse("https://via.placeholder.com/150?text=Photo+$index")
        )
    }

    PhotoGallery(
        photoList = fakePhotos,
        selectedPhotoId = 3,
        onPhotoClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPhotoItem() {
    val fakeUri = Uri.parse("https://via.placeholder.com/150")

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        PhotoItem(photoUri = fakeUri, isSelected = false, onPhotoClick = {})
        PhotoItem(photoUri = fakeUri, isSelected = true, onPhotoClick = {})
    }
}