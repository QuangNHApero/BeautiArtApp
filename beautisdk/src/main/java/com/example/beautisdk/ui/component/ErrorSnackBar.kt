package com.example.beautisdk.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.beautisdk.R
import com.example.beautisdk.ui.design_system.AppTheme
import com.example.beautisdk.ui.design_system.LocalCustomTypography
import com.example.beautisdk.ui.design_system.component.AperoTextView
import com.example.beautisdk.ui.design_system.pxToDp

@Composable
fun CustomSnackbar(
    textResId: Int = R.string.snackbar_error_network,
    isError: Boolean = true,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = if (isError) Color.Red else Color.Blue)
            .padding(horizontal = 23.pxToDp(), vertical = 20.pxToDp())
    ) {
        AperoTextView(
            text = stringResource(textResId),
            textStyle = LocalCustomTypography.current.Footnote.medium.copy(color = Color.White)
        )
    }
}

@Preview(
    name = "Error Snackbar",
    showBackground = true,
    backgroundColor = 0xFFF5F5F5,
    widthDp = 360,
    heightDp = 640
)
@Composable
fun PreviewErrorSnackbar() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            CustomSnackbar(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}