package com.example.pokemondex.view.new_dex

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.pokemondex.R
import com.example.pokemondex.ui.theme.Blue
import com.example.pokemondex.ui.theme.MainColor
import com.example.pokemondex.ui.theme.SubColor
import com.example.pokemondex.ui.theme.White
import com.example.pokemondex.util.*
import com.example.pokemondex.view.navigation.RouteAction

val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDexListScreen(routAction: RouteAction) {
    val lazyListSate = rememberLazyListState()

    Scaffold(
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                NewDexHeader(lazyListSate)
                NewDexBody(lazyListSate, Modifier.weight(1f))
                NewDexFooter()
            }
        }
    )
}

@Composable
fun NewDexHeader(lazyListSate: LazyListState) {
    val imageHeight = animateDpAsState(targetValue = if (lazyListSate.isScrolled) 0.dp else 172.dp)
    val textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_arceus_logo),
            contentDescription = "logo",
            Modifier
                .size(width = 217.dp, height = imageHeight.value)
                .align(Alignment.CenterHorizontally)
        )

        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_ball_fill),
                contentDescription = "ball"
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "일반 241/241", style = textStyle)

            Spacer(modifier = Modifier.width(10.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_ball_shiny),
                contentDescription = "ball"
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "이로치 241/241", style = textStyle)
        }
    }
}

@Composable
fun NewDexBody(lazyListSate: LazyListState, modifier: Modifier = Modifier) {
    val numbers = remember { List(size = 25) { it } }

    LazyColumn(state = lazyListSate, modifier = modifier) {
        gridItems(
            data = numbers,
            columnCount = 2,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 24.dp)
        ) { item, _ ->
            NewDexListItem()
        }
    }
}

@Composable
fun NewDexListItem() {

    val textStyle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = getBlack()
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = getSkyBlue()
            ),
            border = BorderStroke(1.dp, Blue),
            shape = RoundedCornerShape(10.dp)
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

                val (image, number, name, normal, shiny, star) = createRefs()

                Image(
                    painter = painterResource(id = R.drawable.img_rock),
                    contentDescription = "image",
                    modifier = Modifier
                        .size(56.dp)
                        .constrainAs(image) {
                            top.linkTo(parent.top, 4.dp)
                            start.linkTo(parent.start, 7.dp)
                            bottom.linkTo(parent.bottom, 4.dp)
                        }
                )

                Text(
                    text = "001",
                    style = textStyle,
                    fontSize = 10.sp,
                    modifier = Modifier.constrainAs(number) {
                        top.linkTo(parent.top, 5.dp)
                        start.linkTo(image.end, 2.dp)
                    })

                Text(
                    text = "피카츄",
                    style = textStyle,
                    modifier = Modifier.constrainAs(name) {
                        top.linkTo(number.bottom)
                        start.linkTo(number.start)
                    }
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_ball_fill),
                    contentDescription = "normal",
                    modifier = Modifier.constrainAs(normal) {
                        top.linkTo(name.bottom)
                        start.linkTo(name.start)
                    }
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_ball_shiny),
                    contentDescription = "shiny",
                    modifier = Modifier.constrainAs(shiny) {
                        top.linkTo(normal.top)
                        start.linkTo(normal.end, 3.dp)
                    }
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_star_fill),
                    contentDescription = "star",
                    modifier = Modifier.constrainAs(star) {
                        top.linkTo(shiny.top)
                        start.linkTo(shiny.end, 3.dp)
                    }
                )

            }
        }
    }
}

@Composable
fun NewDexFooter() {
    val radioSelected = remember { mutableStateOf("전체") }
    val valueState = remember { mutableStateOf("") }
    var isOpen by remember { mutableStateOf(false) }
    val rotateValue by animateFloatAsState(targetValue = if (isOpen) 0f else 180f)

    val radioItems = listOf("전체", "미포획", "즐겨찾기")
    val isSelectedItem: (String) -> Boolean = { radioSelected.value == it }
    val onSelectedChange: (String) -> Unit = { radioSelected.value = it }

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MainColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .clipToBounds()
                .clickable { isOpen = isOpen.not() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = "arrow",
                tint = White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(45.dp)
                    .rotate(rotateValue)
            )
        }

        AnimatedVisibility(visible = isOpen) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SubColor)
            ) {
                SearchTextField(
                    value = valueState.value,
                    onValueChange = { valueState.value = it },
                    onSearch = {},
                    modifier = Modifier.padding(0.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    radioItems.forEach {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = isSelectedItem(it),
                                onClick = { onSelectedChange(it) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Blue,
                                    unselectedColor = White
                                )
                            )
                            Text(
                                text = it,
                                style = TextStyle(fontSize = 14.sp, color = White),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.nonRippleClickable {
                                    onSelectedChange(it)
                                }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }

            }
        }
    }
}