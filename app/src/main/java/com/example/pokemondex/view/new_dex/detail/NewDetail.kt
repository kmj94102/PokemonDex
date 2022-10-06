package com.example.pokemondex.view.new_dex.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.*
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
        NewDetailHeader(routeAction, viewModel)
        NewDetailBody(viewModel.pokemonInfo.value, viewModel.evolutionInfo)
    }

}

@Composable
fun NewDetailHeader(
    routeAction: RouteAction,
    viewModel: NewDetailViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        NewDetailTitle(
            beforeInfo = viewModel.beforeButtonInfo.value,
            afterInfo = viewModel.afterButtonInfo.value,
            pokemonInfo = viewModel.pokemonInfo.value,
            routeAction = routeAction,
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
        )
        PokemonInfo(
            pokemonInfo = viewModel.pokemonInfo.value,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

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

@Composable
fun PokemonInfo(
    pokemonInfo: CollectionPokemonDetail,
    modifier: Modifier = Modifier
) {
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
            modifier = Modifier.constrainAs(importance) {
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
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(
                            Color(if (pokemonInfo.normal) 0x0 else 0xBF000000),
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
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(
                            Color(if (pokemonInfo.shiny) 0x0 else 0xBF000000),
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

@Composable
fun NewDetailBody(pokemonInfo: CollectionPokemonDetail, evolution: List<Evolution>) {
    var selectIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth()) {
        NewDetailTab(selectIndex) {
            selectIndex = it
        }
        PokemonProfile(pokemonInfo, evolution)
    }

}

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

@Composable
fun PokemonProfile(pokemonInfo: CollectionPokemonDetail, evolution: List<Evolution>) {
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
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Text(text = "프로필", style = titleStyle)
        }

        item {
            Text(text = "설명", style = contentStyle, fontWeight = FontWeight.Bold, color = MainColor)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = pokemonInfo.description, style = contentStyle)
        }

        item {
            Text(text = "분류", style = contentStyle, fontWeight = FontWeight.Bold, color = MainColor)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = pokemonInfo.classification, style = contentStyle)
        }

        item {
            Text(text = "특성", style = contentStyle, fontWeight = FontWeight.Bold, color = MainColor)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = pokemonInfo.characteristic, style = contentStyle)
        }

        item {
            Text(text = "스테이터스", style = titleStyle)
            Spacer(modifier = Modifier.height(5.dp))

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