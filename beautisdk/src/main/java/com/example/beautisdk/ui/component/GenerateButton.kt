package com.example.beautisdk.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.beautisdk.ui.design_system.LocalCustomTypography
import com.example.beautisdk.ui.design_system.pxToDp

@Composable
fun CustomGradientButton(
    isEnabled: Boolean = false,
    onClick: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    contentButton : @Composable () -> Unit = {},
) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFFE400D9), Color(0xFF0000FF))
    )
    val disabledBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFFE400D9), Color(0xFF0000FF)).map { it.copy(alpha = 0.3f) }
    )

    Box(
        modifier = modifier
            .background(
                brush = if (isEnabled) gradientBrush else disabledBrush,
                shape = RoundedCornerShape(16.pxToDp())
            )
            .clip(RoundedCornerShape(16.pxToDp()))
            .clickable(enabled = isEnabled) { onClick() }
            .padding(vertical = 16.pxToDp()),
        contentAlignment = Alignment.Center
    ) {
        contentButton()
    }
}

@Preview(
    name = "GenerateButton Enabled",
    showBackground = true,
    widthDp = 360,
    heightDp = 64
)@Composable
fun PreviewGenerateButtonEnabled() {
    CustomGradientButton(
        isEnabled = true,
        onClick = {}
    ) {
        Text(
            text = "Generate",
            style = LocalCustomTypography.current.Body.bold.copy(color = Color.White)
        )
    }
}

@Preview(
    name = "GenerateButton Disabled",
    showBackground = true,
    widthDp = 360,
    heightDp = 64
)@Composable
fun PreviewGenerateButtonDisabled() {
    CustomGradientButton(
        isEnabled = false,
        onClick = {}
    ) {
        Text(
            text = "Generate",
            style = LocalCustomTypography.current.Body.bold.copy(color = Color.White)
        )
    }
}
