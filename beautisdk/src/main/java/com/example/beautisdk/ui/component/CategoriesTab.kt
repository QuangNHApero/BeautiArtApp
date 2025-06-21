package com.example.beautisdk.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.beautisdk.R
import com.example.beautisdk.ui.data.model.CategoryArt
import com.example.beautisdk.ui.data.model.StyleArt
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
    val currentCategoryIds = remember(uiState.categories) {
        uiState.categories.map { it._id }
    }

    // ❷ Cache state, truyền danh sách id để tự dọn
    val getStateForCategory = rememberStyleListStateCache(currentCategoryIds)

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
                listState = getStateForCategory(it._id),
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
        modifier = modifier.padding(end = 24.pxToDp()),
        horizontalArrangement = Arrangement.spacedBy(16.pxToDp()),
        contentPadding = PaddingValues(horizontal = 0.pxToDp())
    ) {
        itemsIndexed(
            items = categories,
            key = { _, cat -> cat._id }
        ) { index, category ->

            val isSelected = index == selectedIndex

            Column(
                modifier = Modifier
                    .width(50.pxToDp())
                    .clickable { onCategorySelected(index) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AperoTextView(
                    text = category.name,
                    textStyle = LocalCustomTypography.current.Caption1.bold.copy(
                        color = if (isSelected) selectedColor else unselectedColor
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    marqueeEnabled = true
                )
                if (isSelected) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.pxToDp()),
                        thickness = 2.dp,
                        color = selectedColor
                    )
                } else {
                    Spacer(Modifier.height(2.dp))
                }
            }
        }
    }
}



@Composable
fun StyleList(
    styles: List<StyleArt>,
    selectedStyleId: String?,
    selectedColor: Color,
    listState: LazyListState,
    onStyleSelected: (String) -> Unit
) {
    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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
                placeholder = painterResource(R.drawable.ic_photo),
                error = painterResource(R.drawable.ic_photo),
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

@Composable
fun rememberStyleListStateCache(currentCategoryIds: List<String>): (String) -> LazyListState {
    // mutableStateMap giúp Compose biết khi map thay đổi (ít khi cần, nhưng an toàn)
    val cache = remember { mutableStateMapOf<String, LazyListState>() }

    LaunchedEffect(currentCategoryIds) {
        cache.keys.removeAll { it !in currentCategoryIds }
    }
    // Trả về hàm lấy State theo id
    return remember {
        { id: String ->
            cache.getOrPut(id) { LazyListState() }
        }
    }
}
