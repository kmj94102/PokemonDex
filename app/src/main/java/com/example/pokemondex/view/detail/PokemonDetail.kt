package com.example.pokemondex.view.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokemondex.R
import com.example.pokemondex.network.data.PokemonItem
import com.example.pokemondex.network.data.getTypeImage
import com.example.pokemondex.ui.theme.Black
import com.example.pokemondex.ui.theme.Typography
import com.example.pokemondex.util.CustomScrollableTabRow
import com.example.pokemondex.util.getBlack
import com.example.pokemondex.util.getWhite
import com.example.pokemondex.util.nonRippleClickable
import com.example.pokemondex.view.navigation.RouteAction
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DetailContainer(
    routeAction: RouteAction,
    viewModel: DetailViewModel = hiltViewModel()
) {

    val info = viewModel.pokemonInfo.value
    val isShiny = remember { mutableStateOf(false) }

    val tabData = listOf(
        TabItem.Description(info),
        TabItem.Status(info.status),
        TabItem.TypeCompatibility(viewModel.typeCompatibility),
        TabItem.EvolutionContainer
    )
    val pagerState = rememberPagerState(initialPage = 0)
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF6A8))
    ) {
        val (titleRow, txtName, attributeRow, mainImage,
            isShinyButton, infoCard) = createRefs()
        /** 상단 타이틀 **/
        Box(
            Modifier
                .fillMaxWidth()
                .height(58.dp)
                .constrainAs(titleRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            if (info.before != null) {
                Image(
                    painter = painterResource(id = R.drawable.ic_prev),
                    contentDescription = "prev",
                    colorFilter = ColorFilter.tint(Black),
                    modifier = Modifier
                        .padding(17.dp)
                        .align(Alignment.CenterStart)
                        .nonRippleClickable {
                            routeAction.navToDetail(info.before.number, true)
                        }
                )

                AsyncImage(
                    model = if (isShiny.value) info.before.dotShinyImage else info.before.dotImage,
                    contentDescription = "이미지",
                    error = painterResource(id = R.drawable.img_monsterbal),
                    placeholder = painterResource(id = R.drawable.img_monsterbal),
                    modifier = Modifier
                        .padding(start = 50.dp)
                        .align(Alignment.CenterStart)
                        .size(42.dp)
                        .nonRippleClickable {
                            routeAction.navToDetail(info.before.number, true)
                        }
                )
            }

            Text(
                text = "#${info.number}",
                textAlign = TextAlign.Center,
                style = Typography.titleLarge,
                color = Black,
                fontSize = 24.sp,
                modifier = Modifier
                    .align(Alignment.Center)
            )

            if (info.after != null) {
                AsyncImage(
                    model = if (isShiny.value) info.after.dotShinyImage else info.after.dotImage,
                    contentDescription = "이미지",
                    error = painterResource(id = R.drawable.img_monsterbal),
                    placeholder = painterResource(id = R.drawable.img_monsterbal),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(top = 8.dp, end = 50.dp)
                        .size(42.dp)
                        .nonRippleClickable {
                            routeAction.navToDetail(info.after.number, true)
                        }
                )

                Image(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "next",
                    colorFilter = ColorFilter.tint(Black),
                    modifier = Modifier
                        .padding(17.dp)
                        .align(Alignment.CenterEnd)
                        .nonRippleClickable {
                            routeAction.navToDetail(info.after.number, true)
                        }
                )
            }
        } // 상단 타이틀

        /** 이름 **/
        Text(
            text = info.name,
            style = Typography.titleLarge,
            fontSize = 24.sp,
            color = getBlack(),
            modifier = Modifier.constrainAs(txtName) {
                top.linkTo(titleRow.bottom)
                start.linkTo(parent.start, 24.dp)
            }
        ) // 이름

        /** 속성 **/
        Row(modifier = Modifier
            .constrainAs(attributeRow) {
                start.linkTo(txtName.start)
                top.linkTo(txtName.bottom)
            }
        ) {
            info.attribute.split(",").forEach {
                Image(
                    painter = painterResource(id = getTypeImage(it)),
                    contentDescription = "type",
                    modifier = Modifier.size(43.dp)
                )
            }
        } // 속성

        /** 이로치 변경 버튼 **/
        Image(
            painter = painterResource(
                if (isShiny.value) R.drawable.ic_shiny
                else R.drawable.ic_none_shiny
            ),
            contentDescription = "shiny change",
            modifier = Modifier
                .constrainAs(isShinyButton) {
                    top.linkTo(attributeRow.top)
                    end.linkTo(parent.end, 24.dp)
                }
                .nonRippleClickable {
                    isShiny.value = isShiny.value.not()
                }
        )

        Card(
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
            colors = CardDefaults.cardColors(
                containerColor = getWhite()
            ),
            modifier = Modifier.constrainAs(infoCard) {
                top.linkTo(mainImage.bottom, (-27).dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        ) {
            CustomScrollableTabRow(
                tabs = tabData.map { it.name },
                selectedTabIndex = tabIndex,
            ) { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }



            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
                count = tabData.size
            ) { index ->
                tabData[index].screenToLoad()
            }

        }

        /** 메인 이미지 **/
        AsyncImage(
            model = if (isShiny.value) info.shinyImage
            else info.image,
            contentDescription = "main Image",
            error = painterResource(id = R.drawable.img_monsterbal),
            placeholder = painterResource(id = R.drawable.img_monsterbal),
            modifier = Modifier
                .size(200.dp)
                .constrainAs(mainImage) {
                    top.linkTo(txtName.bottom, 31.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}

sealed class TabItem(val name: String, val screenToLoad: @Composable () -> Unit) {
    data class Description(
        val info: PokemonItem
    ) : TabItem(
        name = "설명",
        screenToLoad = {
            DescriptionContainer(
                info = info,
                modifier = Modifier.fillMaxSize()
            )
        }
    )

    data class Status(
        val status: String
    ) : TabItem(
        name = "스테이터스",
        screenToLoad = {
            StatusContainer(
                status = status,
                modifier = Modifier.fillMaxSize()
            )
        }
    )

    data class TypeCompatibility(
        val list: List<Pair<Float, String>>
    ) : TabItem(
        name = "상성",
        screenToLoad = {
            TypeCompatibilityContainer(
                list = list,
                modifier = Modifier.fillMaxSize()
            )
        }
    )

    object EvolutionContainer : TabItem(
        name = "진화",
        screenToLoad = {
            EvolutionContainer(
                modifier = Modifier.fillMaxSize()
            )
        }
    )
}