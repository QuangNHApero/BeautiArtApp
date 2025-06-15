package com.example.beautisdk.ui.design_system.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.beautisdk.R
import com.example.beautisdk.ui.design_system.AppTheme
import com.example.beautisdk.ui.design_system.LocalCustomTypography

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderResId: Int = R.string.textfield_placeholder,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (value.isBlank()) {
            AperoTextView(
                text = stringResource(placeholderResId),
                textStyle = LocalCustomTypography.current.Footnote.medium.copy(
                    color = Color(
                        0x66000000
                    )
                ),
                modifier = Modifier
                    .matchParentSize()
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = LocalCustomTypography.current.Footnote.medium.copy(color = Color.Black),
            modifier = modifier
                .fillMaxWidth()
        )
    }
}

@Preview(name = "MyTextField - 3 lines", showBackground = true, widthDp = 360)
@Composable
fun MyTextFieldPreview_MultiLine() {
    AppTheme {
        MyTextField(
            value = "This is a long text that spans across\nmultiple lines to test\ntext wrapping behavior.",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview(name = "MyTextField - 1 line", showBackground = true, widthDp = 360)
@Composable
fun MyTextFieldPreview_OneLine() {
    AppTheme {
        MyTextField(
            value = "Short line",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview(name = "MyTextField - Empty", showBackground = true, widthDp = 360)
@Composable
fun MyTextFieldPreview_Empty() {
    AppTheme {
        MyTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}


