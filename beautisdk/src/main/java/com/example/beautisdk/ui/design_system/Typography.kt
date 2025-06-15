package com.example.beautisdk.ui.design_system

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.beautisdk.R

private val CustomFontFamily = FontFamily(
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal)
)


fun Float.pxToSp(fontScale: Float) = this * fontScale

data class TitleTypography(
    val bold: TextStyle,
    val semiBold: TextStyle,
    val medium: TextStyle,
    val regular: TextStyle,
    val special: TextStyle
)

val LocalCustomTypography = compositionLocalOf { CustomTypography.Default }

abstract class CustomTypography {
    abstract val LargeTitle: TitleTypography
    abstract val Title1: TitleTypography
    abstract val Title2: TitleTypography
    abstract val Title3: TitleTypography
    abstract val Headline: TitleTypography
    abstract val Body: TitleTypography
    abstract val Footnote: TitleTypography
    abstract val Caption1: TitleTypography
    abstract val Caption2: TitleTypography

    companion object {
        val Default = createTypography { fontSize, lineHeight ->
            TitleTypography(
                createTextStyle(FontWeight.Bold, fontSize, lineHeight),
                createTextStyle(FontWeight.SemiBold, fontSize, lineHeight),
                createTextStyle(FontWeight.Medium, fontSize, lineHeight),
                createTextStyle(FontWeight.Normal, fontSize, lineHeight),
                createTextStyle(
                    weight = FontWeight.Normal,
                    fontSize = fontSize,
                    lineHeight = lineHeight,
                ),
            )
        }

        private fun createTextStyle(
            weight: FontWeight,
            fontSize: Float,
            lineHeight: Float,
            fontFamily: FontFamily = CustomFontFamily
        ) = TextStyle(
            fontFamily = fontFamily,
            fontWeight = weight,
            fontSize = fontSize.sp,
            lineHeight = lineHeight.sp
        )
    }
}

private fun createTypography(
    createTitleTypography: (fontSize: Float, lineHeight: Float) -> TitleTypography
): CustomTypography = object : CustomTypography() {
    override val LargeTitle = createTitleTypography(34f, 41f)
    override val Title1 = createTitleTypography(28f, 34f)
    override val Title2 = createTitleTypography(22f, 28f)
    override val Title3 = createTitleTypography(20f, 25f)
    override val Headline = createTitleTypography(18f, 24f)
    override val Body = createTitleTypography(16f, 24f)
    override val Footnote = createTitleTypography(14f, 20f)
    override val Caption1 = createTitleTypography(12f, 16f)
    override val Caption2 = createTitleTypography(10f, 12f)
}

@Composable
fun rememberCustomTypography(screenScale: Float): CustomTypography {
    val context = LocalContext.current

    return remember(context, screenScale) {
        createTypography { fontSize, lineHeight ->
            TitleTypography(
                createScaledTextStyle(screenScale, FontWeight.Bold, fontSize, lineHeight),
                createScaledTextStyle(screenScale, FontWeight.SemiBold, fontSize, lineHeight),
                createScaledTextStyle(screenScale, FontWeight.Medium, fontSize, lineHeight),
                createScaledTextStyle(screenScale, FontWeight.Normal, fontSize, lineHeight),
                createScaledTextStyle(screenScale, FontWeight.Normal, fontSize, lineHeight),
            )
        }
    }
}

private fun createScaledTextStyle(
    fontScale: Float,
    weight: FontWeight,
    fontSize: Float,
    lineHeight: Float,
    fontFamily: FontFamily = CustomFontFamily
) = TextStyle(
    fontFamily = fontFamily,
    fontWeight = weight,
    fontSize = fontSize.pxToSp(fontScale).sp,
    lineHeight = lineHeight.pxToSp(1f).sp
)