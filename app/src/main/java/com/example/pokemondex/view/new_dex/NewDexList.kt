package com.example.pokemondex.view.new_dex

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.pokemondex.R
import com.example.pokemondex.ui.theme.Blue
import com.example.pokemondex.util.getBlack
import com.example.pokemondex.util.getSkyBlue
import com.example.pokemondex.util.getWhite
import com.example.pokemondex.util.gridItems


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDexListScreen() {
    val lazyListSate = rememberLazyListState()

    Scaffold(
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                NewDexHeader(lazyListSate)
                NewDexBody(lazyListSate)
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
fun NewDexBody(lazyListSate: LazyListState) {
    val topBarHeight = 56.dp
    val numbers = remember { List(size = 25) { it } }
    val padding by animateDpAsState(
        targetValue = if (lazyListSate.isScrolled) 20.dp else topBarHeight,
        animationSpec = tween(durationMillis = 300)
    )

    LazyColumn(state = lazyListSate) {
        gridItems(
            data = numbers,
            columnCount = 2,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 24.dp)
        ) { item, _ ->
            NumberHolder()
        }
    }
}

@Composable
fun NumberHolder() {
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
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = getBlack()
                    ),
                    modifier = Modifier.constrainAs(number) {
                        top.linkTo(parent.top, 5.dp)
                        start.linkTo(image.end, 2.dp)
                    })

            }
        }
    }
}

val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0