package com.example.pokemondex.view.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokemondex.R
import com.example.pokemondex.network.data.Evolution
import com.example.pokemondex.network.data.PokemonItem
import com.example.pokemondex.network.data.getTypeColorList
import com.example.pokemondex.network.data.getTypeImage
import com.example.pokemondex.ui.theme.Black
import com.example.pokemondex.ui.theme.Typography
import com.example.pokemondex.util.CustomScrollableTabRow
import com.example.pokemondex.util.getWhite
import com.example.pokemondex.util.nonRippleClickable
import com.example.pokemondex.view.dialog.ConfirmDialog
import com.example.pokemondex.view.dialog.LoadingDialog
import com.example.pokemondex.view.navigation.RouteAction
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@Composable
fun DetailScreen(
    routeAction: RouteAction,
    viewModel: DetailViewModel = hiltViewModel(),
) {

    val info = viewModel.pokemonInfo.value
    val isShiny = viewModel.isShiny.value
    val stateCollector = viewModel.eventFlow.collectAsState()
    val isLoading = remember { mutableStateOf(false) }
    val isError = remember { mutableStateOf(false) }
    val typeList = info.attribute.split(",").toMutableList()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.horizontalGradient(getTypeColorList(typeList))
            )
    ) {
        val (header, body, footer) = createRefs()
        /** 타이틀 **/
        DetailHeader(
            routeAction = routeAction,
            info = info,
            isShiny = isShiny,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .constrainAs(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) // 타이틀

        /** 포켓몬 정보 표시 **/
        DetailFooter(
            info = info,
            viewModel = viewModel,
            isShiny = isShiny,
            modifier = Modifier.constrainAs(footer) {
                top.linkTo(body.bottom, (-27).dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        ) // DetailFooter

        /** 포켓몬 이름, 속성, 이미지 및 이로치 변경 버튼 표시 **/
        DetailBody(
            info = info,
            viewModel = viewModel,
            isShiny = isShiny,
            modifier = Modifier.constrainAs(body) {
                top.linkTo(header.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) // 포켓몬 정보 표시
        
        if (isLoading.value) {
            LoadingDialog(isLoading = isLoading)
        }

        if (isError.value) {
            ConfirmDialog(
                message = stringResource(id = R.string.load_error),
                isShow = isError
            ) {
                isError.value = false
                routeAction.popupBackStack()
            }
        }

    } // ConstraintLayout
    
    when(stateCollector.value) {
        DetailViewModel.Event.Init -> {
            isLoading.value = true
        }
        DetailViewModel.Event.Success -> {
            isLoading.value = false
        }
        DetailViewModel.Event.Failure -> {
            isLoading.value = false
            isError.value = true
        }
    }
}

@Composable
fun DetailHeader(
    routeAction: RouteAction,
    info: PokemonItem,
    isShiny: Boolean,
    modifier: Modifier = Modifier
) {
    /** 상단 타이틀 **/
    Box(modifier = modifier) {
        /** 이전 포켓몬 **/
        if (info.before != null) {
            Image(
                painter = painterResource(id = R.drawable.ic_prev),
                contentDescription = "prev",
                colorFilter = ColorFilter.tint(Black),
                modifier = Modifier
                    .padding(17.dp)
                    .align(Alignment.CenterStart)
                    .nonRippleClickable {
                        routeAction.navToDetailBefore(info.before.number, true, isShiny)
                    }
            )

            AsyncImage(
                model = if (isShiny) info.before.dotShinyImage else info.before.dotImage,
                contentDescription = "이미지",
                error = painterResource(id = R.drawable.img_monsterbal),
                placeholder = painterResource(id = R.drawable.img_monsterbal),
                modifier = Modifier
                    .padding(start = 50.dp)
                    .align(Alignment.CenterStart)
                    .size(42.dp)
                    .nonRippleClickable {
                        routeAction.navToDetail(info.before.number, true, isShiny)
                    }
            )
        } // if: 이전 포켓몬

        /** 포켓몬 번호 **/
        Text(
            text = "#${info.number}",
            textAlign = TextAlign.Center,
            style = Typography.titleLarge,
            color = Black,
            fontSize = 24.sp,
            modifier = Modifier
                .align(Alignment.Center)
        ) // 포켓몬 번호

        /** 다음 포켓몬 **/
        if (info.after != null) {
            AsyncImage(
                model = if (isShiny) info.after.dotShinyImage else info.after.dotImage,
                contentDescription = "이미지",
                error = painterResource(id = R.drawable.img_monsterbal),
                placeholder = painterResource(id = R.drawable.img_monsterbal),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 8.dp, end = 50.dp)
                    .size(42.dp)
                    .nonRippleClickable {
                        routeAction.navToDetail(info.after.number, true, isShiny)
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
                        routeAction.navToDetail(info.after.number, true, isShiny)
                    }
            )
        } // if : 다음 포켓몬
    } // 상단 타이틀
}

@Composable
fun DetailBody(
    info: PokemonItem,
    viewModel: DetailViewModel,
    isShiny: Boolean,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        val (txtName, attributeRow, isShinyButton, mainImage) = createRefs()
        /** 이름 **/
        Text(
            text = info.name,
            style = Typography.titleLarge,
            fontSize = 24.sp,
            color = Black,
            modifier = Modifier.constrainAs(txtName) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
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
                if (isShiny) R.drawable.ic_shiny
                else R.drawable.ic_none_shiny
            ),
            contentDescription = "shiny change",
            modifier = Modifier
                .constrainAs(isShinyButton) {
                    top.linkTo(attributeRow.top)
                    end.linkTo(parent.end)
                }
                .nonRippleClickable {
                    viewModel.changeShinyState()
                }
        ) // 이로치 변경 버튼
        /** 메인 이미지 **/
        AsyncImage(
            model = if (isShiny) info.shinyImage
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
        ) // 메인 이미지
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DetailFooter(
    info: PokemonItem,
    viewModel: DetailViewModel,
    isShiny: Boolean,
    modifier: Modifier = Modifier
) {
    val tabData = listOf(
        TabItem.Description(
            info = info,
            tabName = stringResource(id = R.string.description)
        ),
        TabItem.Status(
            status = info.status,
            tabName = stringResource(id = R.string.status)
        ),
        TabItem.TypeCompatibility(
            list = viewModel.typeCompatibility,
            tabName = stringResource(id = R.string.type_compatibility)
        ),
        TabItem.EvolutionContainer(
            list = info.evolutionList,
            isShiny = isShiny,
            tabName = stringResource(id = R.string.evolution)
        )
    )
    val pagerState = rememberPagerState(initialPage = 0)
    val tabIndex = pagerState.currentPage

    Card(
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        colors = CardDefaults.cardColors(
            containerColor = getWhite()
        ),
        modifier = modifier
    ) {
        /** 탭 **/
        CustomScrollableTabRow(
            tabs = tabData.map { it.name },
            selectedTabIndex = tabIndex,
            pagerState = pagerState
        )

        /** 뷰페이저 **/
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            count = tabData.size
        ) { index ->
            tabData[index].screenToLoad()
        }
    }
}

sealed class TabItem(
    val name: String,
    val screenToLoad: @Composable () -> Unit
) {
    data class Description(
        val info: PokemonItem,
        val tabName: String
    ) : TabItem(
        name = tabName,
        screenToLoad = {
            DescriptionContainer(
                info = info,
                modifier = Modifier.fillMaxSize()
            )
        }
    )

    data class Status(
        val status: String,
        val tabName: String
    ) : TabItem(
        name = tabName,
        screenToLoad = {
            StatusContainer(
                status = status,
                modifier = Modifier.fillMaxSize()
            )
        }
    )

    data class TypeCompatibility(
        val list: List<Pair<Float, String>>,
        val tabName: String
    ) : TabItem(
        name = tabName,
        screenToLoad = {
            TypeCompatibilityContainer(
                list = list,
                modifier = Modifier.fillMaxSize()
            )
        }
    )

    data class EvolutionContainer(
        val list: List<Evolution>,
        val isShiny: Boolean,
        val tabName: String
    ) : TabItem(
        name = tabName,
        screenToLoad = {
            EvolutionContainer(
                list = list,
                isShiny = isShiny,
                modifier = Modifier.fillMaxSize()
            )
        }
    )
}