package com.example.pokemondex.util

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pokemondex.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@Composable
fun getWhite() : Color =
    if (isSystemInDarkTheme()) Black else White

@Composable
fun getBlack() : Color =
    if (isSystemInDarkTheme()) White else Black

@Composable
fun getGray() : Color =
    if (isSystemInDarkTheme()) LightGray else Gray

@Composable
fun getSkyBlue() : Color =
    if (isSystemInDarkTheme()) Black else SkyBlue

@Composable
fun Modifier.nonRippleClickable(
    onClick: () -> Unit
) = clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }

// https://betterprogramming.pub/gridview-and-lazycolum-integration-with-jetpack-compose-e90849aeb6d3
fun <T> LazyListScope.gridItems(
    data: List<T>,
    columnCount: Int,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    itemContent: @Composable BoxScope.(T, Int) -> Unit,
) {
    val size = data.count()
    val rows = if (size == 0) 0 else 1 + (size - 1) / columnCount
    items(rows, key = { it.hashCode() }) { rowIndex ->
        Row(
            horizontalArrangement = horizontalArrangement,
            modifier = modifier
        ) {
            for (columnIndex in 0 until columnCount) {
                val itemIndex = rowIndex * columnCount + columnIndex
                if (itemIndex < size) {
                    Box(
                        modifier = Modifier.weight(1F, fill = true),
                        propagateMinConstraints = true
                    ) {
                        itemContent(data[itemIndex], itemIndex)
                    }
                } else {
                    Spacer(Modifier.weight(1F, fill = true))
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CustomScrollableTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    pagerState: PagerState
) {
    val coroutineScope = rememberCoroutineScope()

    androidx.compose.material.TabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = getWhite(),
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
            )
        },
        divider = {
            Divider(color = getGray(), modifier = Modifier.height(1.dp))
        }
    ) {
        tabs.forEachIndexed { index, tabItem ->
            Tab(
                selected = selectedTabIndex == index,
                selectedContentColor = MainColor,
                unselectedContentColor = getGray(),
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            ) {
                Text(
                    text = tabItem,
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(top = 40.dp, bottom = 8.dp)
                )
            }
        }
    }
}
