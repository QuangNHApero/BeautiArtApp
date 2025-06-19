package com.example.beautisdk.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.aperoaiservice.fetch.domain.model.CategoryArt
import com.example.aperoaiservice.fetch.domain.model.StyleArt
import com.example.beautisdk.R
import com.example.beautisdk.ui.design_system.LocalCustomTypography
import com.example.beautisdk.ui.design_system.component.AperoTextView
import com.example.beautisdk.ui.design_system.pxToDp
import com.example.beautisdk.ui.screen.art.preview.GenerateArtUiState

@Composable
internal fun ChooseStyleScreen(
    uiState: GenerateArtUiState,
    onCategorySelected: (Int) -> Unit,
    onStyleSelected: (String) -> Unit,
    selectedColor: Color,
    unselectedColor: Color,
    selectedBackgroundColor: Color,
    modifier: Modifier = Modifier
) {
    val selectedCategory = uiState.categories.getOrNull(uiState.selectedCategoryIndex)

    Column(modifier = modifier) {
        AperoTextView(
            text = stringResource(R.string.txt_choose_style),
            textStyle = LocalCustomTypography.current.Title3.bold.copy(color = selectedColor),
            maxLines = 1,
            textAlign = TextAlign.Start,
            marqueeEnabled = true
        )
        Spacer(Modifier.height(5.pxToDp()))

        CustomCategoryTabs(
            categories = uiState.categories,
            selectedIndex = uiState.selectedCategoryIndex,
            selectedColor = selectedColor,
            unselectedColor = unselectedColor,
            onCategorySelected = onCategorySelected
        )

        Spacer(Modifier.height(16.pxToDp()))

        selectedCategory?.let {
            StyleList(
                styles = it.styles,
                selectedStyleId = uiState.selectedStyleId,
                selectedColor = selectedBackgroundColor,
                onStyleSelected = onStyleSelected
            )
        }
    }
}

@Composable
fun CustomCategoryTabs(
    categories: List<CategoryArt>,
    selectedIndex: Int,
    selectedColor: Color,
    unselectedColor: Color,
    onCategorySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 0.dp) // Xoá lề hai bên
    ) {
        itemsIndexed(categories) { index, category ->
            val isSelected = index == selectedIndex
            AperoTextView(
                text = category.name,
                textStyle = LocalCustomTypography.current.Caption1.bold.copy(color = if (isSelected) selectedColor else unselectedColor),
                modifier = Modifier
                    .clickable { onCategorySelected(index) }
                    .drawBehind {
                        if (isSelected) {
                            val strokeWidth = 2.pxToDp().toPx()
                            val y = size.height
                            drawLine(
                                color = selectedColor,
                                start = Offset(25f, y + 5f),
                                end = Offset(size.width - 25f, y + 5f),
                                strokeWidth = strokeWidth
                            )
                        }
                    }
            )
        }
    }
}


@Composable
fun StyleList(
    styles: List<StyleArt>,
    selectedStyleId: String?,
    selectedColor: Color,
    onStyleSelected: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(styles) { style ->
            val isSelected = style._id == selectedStyleId
            StyleItem(
                style = style,
                isSelected = isSelected,
                selectedColor = selectedColor,
                onClick = { onStyleSelected(style._id) }
            )
        }
    }
}

@Composable
fun StyleItem(
    style: StyleArt,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(80.pxToDp())
                .clip(RoundedCornerShape(16.pxToDp()))
        ) {
            AsyncImage(
                model = style.thumbnail,
                contentDescription = style.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(16.pxToDp()))
            )
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(selectedColor.copy(alpha = 0.2f))
                        .border(
                            width = 3.pxToDp(),
                            color = selectedColor,
                            shape = RoundedCornerShape(16.pxToDp())
                        )
                        .clip(RoundedCornerShape(16.pxToDp()))
                )
            }
        }
        Spacer(modifier = Modifier.height(2.pxToDp()))
        AperoTextView(
            text = style.name,
            textStyle = LocalCustomTypography.current.Caption1.medium.copy(color = if (isSelected) selectedColor else Color.Black),
            maxLines = 1,
            marqueeEnabled = true,
            )
    }
}
