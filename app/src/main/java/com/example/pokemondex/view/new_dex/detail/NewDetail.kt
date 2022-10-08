package com.example.pokemondex.view.new_dex.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokemondex.R
import com.example.pokemondex.database.CollectionPokemonDetail
import com.example.pokemondex.database.PokemonButtonInfo
import com.example.pokemondex.network.data.Evolution
import com.example.pokemondex.network.data.StatusInfo
import com.example.pokemondex.network.data.getTypeColor
import com.example.pokemondex.network.data.getTypeImage
import com.example.pokemondex.ui.theme.Black
import com.example.pokemondex.ui.theme.MainColor
import com.example.pokemondex.ui.theme.Typography
import com.example.pokemondex.ui.theme.White
import com.example.pokemondex.util.*
import com.example.pokemondex.view.detail.CustomProgressBar
import com.example.pokemondex.view.detail.EvolutionRow
import com.example.pokemondex.view.navigation.RouteAction
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.launch

@Composable
fun NewDetailScreen(
    routeAction: RouteAction,
    viewModel: NewDetailViewModel = hiltViewModel()
) {

    val type = viewModel.pokemonInfo.value.attribute.split(",").first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(getTypeColor(type)))
    ) {
        /** 헤더 영역 : 번호, 이전/다음 포켓몬, 포켓몬 이름/타입/이미지 **/
        NewDetailHeader(routeAction, viewModel)
        /** 바디 영역 : 포켓몬 정보 **/
        NewDetailBody(viewModel.pokemonInfo.value, viewModel.evolutionInfo)
    }

}

/** 헤더 영역 : 번호, 이전/다음 포켓몬, 포켓몬 이름/타입/이미지 **/
@Composable
fun NewDetailHeader(
    routeAction: RouteAction,
    viewModel: NewDetailViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        /** 타이틀 영역 : 번호, 이전/다음 포켓몬 **/
        NewDetailTitle(
            beforeInfo = viewModel.beforeButtonInfo.value,
            afterInfo = viewModel.afterButtonInfo.value,
            pokemonInfo = viewModel.pokemonInfo.value,
            routeAction = routeAction,
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
        )
        /** 포켓몬 정보 : 포켓몬 이름/타입/이미지 **/
        PokemonInfo(
            pokemonInfo = viewModel.pokemonInfo.value,
            viewModel = viewModel,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/** 타이틀 영역 : 번호, 이전/다음 포켓몬 **/
@Composable
fun NewDetailTitle(
    beforeInfo: PokemonButtonInfo?,
    afterInfo: PokemonButtonInfo?,
    pokemonInfo: CollectionPokemonDetail,
    routeAction: RouteAction,
    modifier: Modifier = Modifier
) {
    /** 상단 타이틀 **/
    Box(modifier = modifier) {
        /** 이전 포켓몬 **/
        beforeInfo?.let {
            Image(
                painter = painterResource(id = R.drawable.ic_prev),
                contentDescription = "prev",
                colorFilter = ColorFilter.tint(Black),
                modifier = Modifier
                    .padding(17.dp)
                    .align(Alignment.CenterStart)
                    .nonRippleClickable {
                        routeAction.navToArceusDetailBefore(it.index, it.allDexNumber)
                    }
            )

            AsyncImage(
                model = it.dotImage,
                contentDescription = "이미지",
                error = painterResource(id = R.drawable.img_monsterbal),
                placeholder = painterResource(id = R.drawable.img_monsterbal),
                modifier = Modifier
                    .padding(start = 50.dp)
                    .align(Alignment.CenterStart)
                    .size(42.dp)
                    .nonRippleClickable {
                        routeAction.navToArceusDetailBefore(it.index, it.allDexNumber)
                    }
            )
        }

        /** 포켓몬 번호 **/
        Text(
            text = "#${pokemonInfo.number.padStart(3, '0')}",
            textAlign = TextAlign.Center,
            style = Typography.titleLarge,
            color = Black,
            fontSize = 24.sp,
            modifier = Modifier
                .align(Alignment.Center)
        ) // 포켓몬 번호

        /** 다음 포켓몬 **/
        afterInfo?.let {
            AsyncImage(
                model = afterInfo.dotImage,
                contentDescription = "이미지",
                error = painterResource(id = R.drawable.img_monsterbal),
                placeholder = painterResource(id = R.drawable.img_monsterbal),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 8.dp, end = 50.dp)
                    .size(42.dp)
                    .nonRippleClickable {
                        routeAction.navToArceusDetail(afterInfo.index, afterInfo.allDexNumber, true)
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
                        routeAction.navToArceusDetail(afterInfo.index, afterInfo.allDexNumber, true)
                    }
            )
        }
    } // 상단 타이틀
}

/** 포켓몬 정보 : 포켓몬 이름/타입/이미지 **/
@Composable
fun PokemonInfo(
    pokemonInfo: CollectionPokemonDetail,
    viewModel: NewDetailViewModel,
    modifier: Modifier = Modifier
) {
    val normalState by animateColorAsState(
        targetValue = if (pokemonInfo.normal) Color(0x0) else Color(0xBF000000)
    )
    val shinyState by animateColorAsState(
        targetValue = if (pokemonInfo.shiny) Color(0x0) else Color(0xBF000000)
    )

    ConstraintLayout(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    bottomStart = 30.dp,
                    bottomEnd = 30.dp
                )
            )
            .background(White)
    ) {
        val (name, type, importance, image, shinyImage) = createRefs()

        Text(
            text = pokemonInfo.name,
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Black),
            modifier = Modifier.constrainAs(name) {
                top.linkTo(parent.top, 15.dp)
                start.linkTo(parent.start, 20.dp)
            }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.constrainAs(type) {
                top.linkTo(name.top)
                start.linkTo(name.end, 7.dp)
            }
        ) {
            pokemonInfo.attribute.split(",").forEach {
                Image(
                    painter = painterResource(id = getTypeImage(it)),
                    contentDescription = "type",
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Image(
            painter = painterResource(
                id = if (pokemonInfo.importance) R.drawable.ic_star_fill else R.drawable.ic_star
            ),
            contentDescription = "importance",
            modifier = Modifier
                .nonRippleClickable {
                    viewModel.event(NewDetailEvent.UpdateImportanceState)
                }
                .constrainAs(importance) {
                    top.linkTo(name.top)
                    end.linkTo(parent.end, 20.dp)
                }
        )

        AsyncImage(
            model = pokemonInfo.image,
            contentDescription = "normal",
            error = painterResource(id = R.drawable.img_monsterbal),
            placeholder = painterResource(id = R.drawable.img_monsterbal),
            modifier = Modifier
                .nonRippleClickable {
                    viewModel.event(NewDetailEvent.UpdateNormalState)
                }
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(
                            normalState,
                            blendMode = BlendMode.SrcAtop
                        )
                    }
                }
                .constrainAs(image) {
                    start.linkTo(parent.start, 20.dp)
                    end.linkTo(shinyImage.start, 10.dp)
                    top.linkTo(name.bottom)
                    bottom.linkTo(parent.bottom, 10.dp)
                    width = Dimension.fillToConstraints
                }
        )

        AsyncImage(
            model = pokemonInfo.shinyImage,
            contentDescription = "shiny",
            error = painterResource(id = R.drawable.img_monsterbal),
            placeholder = painterResource(id = R.drawable.img_monsterbal),
            modifier = Modifier
                .nonRippleClickable {
                    viewModel.event(NewDetailEvent.UpdateShinyState)
                }
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(
                            shinyState,
                            blendMode = BlendMode.SrcAtop
                        )
                    }
                }
                .constrainAs(shinyImage) {
                    start.linkTo(image.end)
                    end.linkTo(parent.end, 20.dp)
                    top.linkTo(image.top)
                    bottom.linkTo(image.bottom)
                    width = Dimension.fillToConstraints
                }
        )

    }
}

/** 바디 영역 : 포켓몬 정보 **/
@Composable
fun NewDetailBody(pokemonInfo: CollectionPokemonDetail, evolution: List<Evolution>) {
    var selectIndex by remember { mutableStateOf(0) }
    val itemsListState = rememberLazyListState()
    val sectionsListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxWidth()) {
        /** 텝 영역 **/
        NewDetailTab(selectIndex) {
            selectIndex = it

            scope.launch {
                itemsListState.animateScrollToItem(it)
            }
        }

        /** 포켓몬 정보 : 프로필/스테이터스/진화/상성 **/
        PokemonProfile(
            pokemonInfo,
            evolution,
            itemsListState
        ) {
            val currentSectionIndex = itemsListState.firstVisibleItemIndex
            if (selectIndex != currentSectionIndex) {
                selectIndex = currentSectionIndex

                scope.launch {
                    sectionsListState.animateScrollToItem(currentSectionIndex)
                }
            }
        }
    }

}

/** 텝 영역 **/
@Composable
fun NewDetailTab(selectIndex: Int, onClick: (Int) -> Unit) {
    val list = listOf("프로필", "스테이터스", "진화", "상성")

    TabRow(
        selectedTabIndex = selectIndex,
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        list.forEachIndexed { index, tabItem ->
            Tab(
                selected = selectIndex == index,
                selectedContentColor = MainColor,
                unselectedContentColor = getGray(),
                onClick = {
                    onClick(index)
                },
            ) {
                Text(
                    text = tabItem,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }

}

/** 포켓몬 정보 : 프로필/스테이터스/진화/상성 **/
@Composable
fun PokemonProfile(
    pokemonInfo: CollectionPokemonDetail,
    evolution: List<Evolution>,
    state: LazyListState,
    onPostScroll: () -> Unit
) {
    val titleStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MainColor
    )

    val contentStyle = TextStyle(
        fontSize = 16.sp,
        color = Black
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(top = 15.dp, bottom = 30.dp, start = 20.dp, end = 20.dp),
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .nestedScroll(object : NestedScrollConnection {
                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    onPostScroll()
                    return super.onPostScroll(consumed, available, source)
                }
            })
    ) {
        item {
            Text(text = "프로필", style = titleStyle)
            Spacer(modifier = Modifier.height(15.dp))

            Text(text = "설명", style = contentStyle, fontWeight = FontWeight.Bold, color = MainColor)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = pokemonInfo.description, style = contentStyle)
            Spacer(modifier = Modifier.height(15.dp))

            Text(text = "분류", style = contentStyle, fontWeight = FontWeight.Bold, color = MainColor)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = pokemonInfo.classification, style = contentStyle)
            Spacer(modifier = Modifier.height(15.dp))

            Text(text = "특성", style = contentStyle, fontWeight = FontWeight.Bold, color = MainColor)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = pokemonInfo.characteristic, style = contentStyle)
        }

        item {
            Text(text = "스테이터스", style = titleStyle)
            Spacer(modifier = Modifier.height(15.dp))

            val list = StatusInfo.values()
            pokemonInfo.status.split(",").forEachIndexed { index, attribute ->
                CustomProgressBar(
                    info = list[index],
                    status = attribute.toIntOrZero(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )
            }
        }

        item {
            Text(text = "진화", style = titleStyle)
            Spacer(modifier = Modifier.height(5.dp))
            EvolutionContainer(evolution)
        }

        item {
            Text(text = "상성", style = titleStyle)
            TypeCompatibilityContainer(
                list = typeCompatibilityInfo(pokemonInfo.attribute)
            )
        }
    }
}

/** 진화 정보 **/
@Composable
fun EvolutionContainer(list: List<Evolution>) {
    list.forEach {
        EvolutionRow(it)
        Spacer(modifier = Modifier.height(25.dp))
    }

    if (list.isEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.img_empty4),
                contentDescription = "empty"
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(id = R.string.empty_evolution),
                style = Typography.bodyLarge,
                color = getBlack(),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

/** 포켓몬 상성 정보 **/
@Composable
fun TypeCompatibilityContainer(
    list: List<Pair<Float, String>>
) {

    val quadrupleDamageList = list.filter { it.first == 4.0f }.map { it.second }
    val doubleDamageList = list.filter { it.first == 2.0f }.map { it.second }
    val damageList = list.filter { it.first == 1.0f }.map { it.second }
    val halfDamageList = list.filter { it.first == 0.5f }.map { it.second }
    val oneQuarterDamageList = list.filter { it.first == 0.25f }.map { it.second }
    val zeroDamageList = list.filter { it.first == 0f }.map { it.second }


    Spacer(modifier = Modifier.height(15.dp))
    if (quadrupleDamageList.isNotEmpty()) {
        DamageItems("X 4", quadrupleDamageList)
    }

    Spacer(modifier = Modifier.height(15.dp))
    if (doubleDamageList.isNotEmpty()) {
        DamageItems("X 2", doubleDamageList)
    }

    Spacer(modifier = Modifier.height(15.dp))
    if (damageList.isNotEmpty()) {
        DamageItems("X 1", damageList)
    }

    Spacer(modifier = Modifier.height(15.dp))
    if (halfDamageList.isNotEmpty()) {
        DamageItems("X 0.5", halfDamageList)
    }

    Spacer(modifier = Modifier.height(15.dp))
    if (oneQuarterDamageList.isNotEmpty()) {
        DamageItems("X 0.25", oneQuarterDamageList)
    }

    Spacer(modifier = Modifier.height(15.dp))
    if (zeroDamageList.isNotEmpty()) {
        DamageItems("X 0", zeroDamageList)
    }
}

/** 포켓몬 상성 아이템 **/
@Composable
fun DamageItems(
    text: String,
    list: List<String>
) {
    Text(
        text = text,
        fontSize = 14.sp,
        style = Typography.bodyLarge,
    )

    FlowRow(
        mainAxisSpacing = 5.dp,
        crossAxisSpacing = 10.dp
    ) {
        list.forEach {
            Image(
                painter = painterResource(id = getTypeImage(it)),
                contentDescription = "Type Icon",
                modifier = Modifier.size(54.dp)
            )
        }
    }
}