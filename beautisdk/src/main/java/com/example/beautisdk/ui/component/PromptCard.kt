package com.example.beautisdk.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.beautisdk.R
import com.example.beautisdk.ui.design_system.AppTheme
import com.example.beautisdk.ui.design_system.component.MyTextField
import com.example.beautisdk.ui.design_system.pxToDp

@Composable
fun PromptCard(
    text: String,
    onValueChange: (String) -> Unit = {},
    onClose: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    colorBorder: Color = Color(0xFFE400D9),
) {
    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 100.pxToDp())
            .border(
                width = 2.pxToDp(),
                color = colorBorder,
                shape = RoundedCornerShape(16.pxToDp())
            )
            .background(Color.White, shape = RoundedCornerShape(12.pxToDp()))
            .padding(15.pxToDp())
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            MyTextField(
                value = text,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(20.pxToDp()))

            Image(
                painter = painterResource(id = R.drawable.ic_remove),
                contentDescription = "remove",
                modifier = Modifier
                    .size(20.pxToDp())
                    .clickable { onClose() }
            )
        }
    }
}

@Preview(
    name = "Prompt Card",
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    widthDp = 360,
    heightDp = 640
)
@Composable
fun PreviewPromptCard() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            PromptCard(
                modifier = Modifier.fillMaxWidth(),
                text = "hello world",
                onClose = {}
            )
        }
    }
}