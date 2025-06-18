package com.example.beautisdk.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.beautisdk.R
import com.example.beautisdk.ui.design_system.LocalCustomTypography
import com.example.beautisdk.ui.design_system.component.AperoTextView
import com.example.beautisdk.ui.design_system.pxToDp

@Composable
fun EmptyContent(modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_photo),
            contentDescription = stringResource(R.string.txt_pickphoto_empty),
            modifier = Modifier
                .size(150.pxToDp())
        )
        Spacer(Modifier.height(16.pxToDp()))
        AperoTextView(
            text = stringResource(R.string.txt_pickphoto_empty),
            textStyle = LocalCustomTypography.current.LargeTitle.bold
                .copy(color = Color.Black),
            textAlign = TextAlign.Center,
            maxLines = 1,
            marqueeEnabled = true
        )
    }
}