package com.example.beautisdk.ui.screen.pick_photo.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.beautisdk.R
import com.example.beautisdk.ui.component.EmptyContent
import com.example.beautisdk.ui.design_system.pxToDp
import com.example.beautisdk.ui.screen.pick_photo.data.PhotoItem

@Composable
fun PhotoGallery(
    photoList: List<PhotoItem>?,
    isLoading: Boolean,
    selectedPhotoId: Long?,
    selectedResId: Int,
    unselectedResId: Int,
    onPhotoClick: (PhotoItem) -> Unit,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState()
) {
    if (photoList.isNullOrEmpty()) {
        if (isLoading) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            EmptyContent(modifier)
        }
        return
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        state = gridState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.pxToDp()),
        verticalArrangement = Arrangement.spacedBy(10.pxToDp()),
        horizontalArrangement = Arrangement.spacedBy(10.pxToDp())
    ) {
        items(
            items = photoList,
            key = { it.id }
        ) { item ->
            PhotoItem(
                photoId = item.id,
                photoUri = item.uri,
                iconResId = if (item.id == selectedPhotoId) selectedResId else unselectedResId,
                onPhotoClick = { onPhotoClick(item) }
            )
        }

        if (isLoading) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}


@Composable
fun PhotoItem(
    photoId: Long,
    photoUri: Uri,
    onPhotoClick: (Long) -> Unit,
    iconResId: Int,
) {
    val context = LocalContext.current

    val request = remember(photoUri) {
        ImageRequest.Builder(context)
            .data(photoUri)
            .placeholder(R.drawable.ic_photo)
            .crossfade(true)
            .build()
    }
    Box(
        modifier = Modifier
            .size(130.pxToDp())
            .clip(RoundedCornerShape(10.pxToDp()))
            .clickable { onPhotoClick(photoId) }
    ) {
        AsyncImage(
            model = request,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )


        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
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
        isLoading = false,
        selectedPhotoId = null,
        onPhotoClick = {},
        selectedResId = R.drawable.ic_pickphoto_selected,
        unselectedResId = R.drawable.ic_pickphoto_unselected
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPhotoGallery() {
    val fakePhotos = List(12) { index ->
        PhotoItem(
            id = index.toLong(),
            uri = Uri.parse("https://via.placeholder.com/150?text=Photo+$index")
        )
    }

    PhotoGallery(
        photoList = fakePhotos,
        isLoading = false,
        selectedPhotoId = 3,
        onPhotoClick = {},
        selectedResId = R.drawable.ic_pickphoto_selected,
        unselectedResId = R.drawable.ic_pickphoto_unselected
    )
}