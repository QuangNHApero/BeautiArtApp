package com.example.beautisdk.ui.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.beautisdk.R
import com.example.beautisdk.ui.design_system.LocalCustomTypography
import com.example.beautisdk.ui.design_system.component.AperoTextView
import com.example.beautisdk.ui.design_system.pxToDp

@Composable
fun PreviewImageCard(
    imageUri: Uri?,
    isResult: Boolean = false,
    modifier: Modifier = Modifier,
    colorBorder: Color = Color(0xFFE400D9),
    onSuccess: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val density = LocalDensity.current
    val screenWidthPx = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val targetSize = (screenWidthPx - 50).toInt()

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.pxToDp()))
            .border(2.pxToDp(), colorBorder, RoundedCornerShape(16.pxToDp()))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize().clickable { onClick() }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_photo),
                    contentDescription = "choose image",
                    modifier = Modifier.size(140.pxToDp())
                )
                Spacer(modifier = Modifier.height(16.pxToDp()))
                AperoTextView(
                    text = stringResource(R.string.txt_add_photo),
                    textStyle = LocalCustomTypography.current.Title3.medium.copy(
                        color = Color(
                            0x66000000
                        )
                    ),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        .size(targetSize, targetSize)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize(),
                    onSuccess = { onSuccess() }
                )
                if (!isResult) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_change_image),
                        contentDescription = "change image",
                        modifier = Modifier
                            .padding(top = 15.pxToDp(), start = 20.pxToDp())
                            .clickable { onClick() }
                            .size(40.pxToDp())
                    )
                }
            }
        }
    }
}


@Preview(name = "PreviewImageCard - Empty", showBackground = true)
@Composable
fun PreviewImageCard_Empty() {
    PreviewImageCard(
        imageUri = null,
        modifier = Modifier.size(120.dp)
    )
}

@Preview(name = "PreviewImageCard - With Image", showBackground = true)
@Composable
fun PreviewImageCard_WithImage() {
    val uri = remember {
        Uri.parse("https://dev-static.apero.vn/video-editor-pro/ai-style-thumbnail/1703903262235-model_thumbnail_realistic_us.jpg")
    }

    PreviewImageCard(
        imageUri = uri,
        modifier = Modifier.size(120.dp)
    )
}