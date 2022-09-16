package com.example.pokemondex.view.home

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemondex.R
import com.example.pokemondex.ui.theme.MainColor
import com.example.pokemondex.ui.theme.Typography
import com.example.pokemondex.util.Constants.generationList
import com.example.pokemondex.util.getBlack
import com.example.pokemondex.util.getWhite
import com.example.pokemondex.util.gridItems
import com.example.pokemondex.view.navigation.RouteAction
import com.example.pokemondex.view.navigation.RouteAction.Companion.UpdateEvolution

@Composable
fun HomeScreen(routeAction: RouteAction) {
    val context = LocalContext.current
    LazyColumn(
        contentPadding = PaddingValues(vertical = 25.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item { HomeHeader() }
        item { Spacer(modifier = Modifier.height(25.dp)) }
        homeBody(routeAction, context)
    }
}

@Composable
fun HomeHeader() {
    /** 홈 화면 타이틀 **/
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.pokedex),
            style = Typography.titleLarge,
            fontSize = 36.sp,
            color = MainColor,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

fun LazyListScope.homeBody(
    routeAction: RouteAction,
    context: Context
) {
    // all ~ 9세대 버튼 이름 및 이미지 리스트
    val list = generationList

    /** 각 세대별 버튼 그리드 형식 표시 **/
    gridItems(
        data = list,
        columnCount = 2,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) { itemData, _ ->
        HomeCardButton(itemData) {
            routeAction.navToList(it.replace(context.getString(R.string.generation), ""))
        }
    } // 각 세대별 버튼 그리드 형식 표시

    /** 포켓몬 등록, 진화 등록 버튼 **/
    item {
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            /** 포켓몬 등록 버튼 **/
            HomeCardButton(
                data = Pair(context.getString(R.string.add_pokemon), R.drawable.img_add_pokemon),
                modifier = Modifier.weight(1f)
            ) {
                routeAction.navToAdd()
            } // 포켓몬 등록 버튼

            Spacer(modifier = Modifier.width(10.dp))

            /** 진화 등록 **/
            HomeCardButton(
                data = Pair(context.getString(R.string.add_evolution), R.drawable.img_add_evolution),
                modifier = Modifier.weight(1f)
            ) {
                routeAction.navToAddEvolution()
            } // 진화 등록
        } // Row
    } // 포켓몬 등록, 진화 등록 버튼

    item { Spacer(modifier = Modifier.height(10.dp)) }

    /** 진화 수정 버튼 **/
    item {
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            /** 진화 수정 버튼 **/
            HomeCardButton(
                data = Pair(context.getString(R.string.evolution_update), R.drawable.img_update_evoluton),
                modifier = Modifier.weight(1f)
            ) {
                routeAction.navToUpdateSearch(RouteAction.UpdateEvolution)
            } // 진화 수정 버튼

            Spacer(modifier = Modifier.width(10.dp))

            /** 진화 수정 버튼 **/
            HomeCardButton(
                data = Pair(context.getString(R.string.new_pokemon_dex), R.drawable.img_update_evoluton),
                modifier = Modifier.weight(1f)
            ) {
                routeAction.navToUpdateSearch(RouteAction.NewPokemonDex)
            } // 진화 수정 버튼

        } // Row
    } // 포켓몬 등록, 진화 등록 버튼
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCardButton(
    data: Pair<String, Int>,
    modifier: Modifier = Modifier,
    clickListener: (String) -> Unit
) {
    Card(
        colors = CardDefaults.elevatedCardColors(
            containerColor = getWhite(),
        ),
        onClick = {
            clickListener(data.first)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(102.dp)
            .shadow(
                elevation = 6.dp,
                spotColor = getBlack(),
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = data.second),
                contentDescription = "image",
                modifier = Modifier
                    .width(151.dp)
                    .height(102.dp)
                    .align(Alignment.BottomEnd)
            )
            Text(
                text = data.first,
                fontSize = 20.sp,
                style = Typography.bodyLarge,
                color = MainColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(13.dp)
            )
        } // Box
    } // Card
}