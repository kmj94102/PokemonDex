package com.example.pokemondex.view.detail

import android.os.Build
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.pokemondex.R
import com.example.pokemondex.network.data.*
import com.example.pokemondex.ui.theme.MainColor
import com.example.pokemondex.ui.theme.Typography
import com.example.pokemondex.ui.theme.White
import com.example.pokemondex.util.*
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun DescriptionContainer(
    info: PokemonItem,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 24.dp)
    ) {
        /** 포켓몬 소개 **/
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = getWhite()
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                ),
                modifier = Modifier
                    .padding(top = 20.dp)
                    .shadow(
                        elevation = 10.dp,
                        spotColor = getBlack(),
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Text(
                    text = info.description,
                    style = Typography.bodyMedium,
                    color = getBlack(),
                    modifier = Modifier.padding(12.dp)
                )
            }
        } // 포켓몬 소개

        item { Spacer(modifier = Modifier.height(16.dp)) }

        /** 포켓몬 분류 **/
        item {
            Text(
                text = stringResource(id = R.string.classification),
                style = Typography.bodyLarge,
                color = MainColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = info.classification,
                style = Typography.bodyMedium,
                color = getBlack()
            )
        } // 포켓몬 분류

        item { Spacer(modifier = Modifier.height(16.dp)) }

        /** 포켓몬 특성 **/
        item {
            Text(
                text = stringResource(id = R.string.characteristic),
                style = Typography.bodyLarge,
                color = MainColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            info.characteristic.split(",").forEach {
                Text(
                    text = it,
                    style = Typography.bodyMedium,
                    color = getBlack(),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    } // 포켓몬 특성
}

@Composable
fun StatusContainer(
    status: String,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(top = 20.dp)
    ) {
        item {
            val list = StatusInfo.values()
            status.split(",").forEachIndexed { index, attribute ->
                CustomProgressBar(
                    info = list[index],
                    status = attribute.toIntOrZero(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 10.dp)
                )
            }
        }
    }
}


@Composable
fun CustomProgressBar(
    info: StatusInfo,
    status: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(22.dp)
    ) {

        Text(
            text = info.koreanName,
            style = Typography.bodyLarge,
            fontSize = 16.sp,
            color = getBlack(),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1.5f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .weight(7f)
                .clip(RoundedCornerShape(22.dp))
        ) {

            val width = if (status / 150f < 0.1f) 0.1f else status / 150f
            val isStart = remember { mutableStateOf(false) }
            val state by animateFloatAsState(
                targetValue = if (isStart.value) width else 0.0f,
                animationSpec = tween(durationMillis = 2500)
            )

            LaunchedEffect(Unit) {
                isStart.value = true
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFFD9D9D9))
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(22.dp))
                    .fillMaxWidth(state)
                    .fillMaxHeight()
                    .background(info.color)
            )

            Text(
                text = "$status",
                style = Typography.bodyLarge,
                fontSize = 14.sp,
                color = White,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun TypeCompatibilityContainer(
    list: List<Pair<Float, String>>,
    modifier: Modifier = Modifier
) {

    val quadrupleDamageList = list.filter { it.first == 4.0f }.map { it.second }
    val doubleDamageList = list.filter { it.first == 2.0f }.map { it.second }
    val damageList = list.filter { it.first == 1.0f }.map { it.second }
    val halfDamageList = list.filter { it.first == 0.5f }.map { it.second }
    val oneQuarterDamageList = list.filter { it.first == 0.25f }.map { it.second }
    val zeroDamageList = list.filter { it.first == 0f }.map { it.second }

    LazyColumn(
        modifier = modifier
            .padding(horizontal = 24.dp)
    ) {
        item { Spacer(modifier = Modifier.height(20.dp)) }

        if (quadrupleDamageList.isNotEmpty()) {
            damageItems("X 4", quadrupleDamageList)
        }

        item { Spacer(modifier = Modifier.height(15.dp)) }

        if (doubleDamageList.isNotEmpty()) {
            damageItems("X 2", doubleDamageList)
        }

        item { Spacer(modifier = Modifier.height(15.dp)) }

        if (damageList.isNotEmpty()) {
            damageItems("X 1", damageList)
        }

        item { Spacer(modifier = Modifier.height(15.dp)) }

        if (halfDamageList.isNotEmpty()) {
            damageItems("X 0.5", halfDamageList)
        }

        item { Spacer(modifier = Modifier.height(15.dp)) }

        if (oneQuarterDamageList.isNotEmpty()) {
            damageItems("X 0.25", oneQuarterDamageList)
        }

        item { Spacer(modifier = Modifier.height(15.dp)) }

        if (zeroDamageList.isNotEmpty()) {
            damageItems("X 0", zeroDamageList)
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}

private fun LazyListScope.damageItems(
    text: String,
    list: List<String>
) {
    item {
        Text(
            text = text,
            fontSize = 14.sp,
            style = Typography.bodyLarge,
        )
    }

    item {
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

}

@Composable
fun EvolutionContainer(
    list: List<Evolution>,
    isShiny: Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 24.dp)
    ) {
        item { Spacer(modifier = Modifier.height(20.dp)) }

        /** 진화 정보 : 이미지, 진화 조건 **/
        item {
            if (list.isEmpty()) return@item

            list.forEach {
                EvolutionRow(it, isShiny)
                Spacer(modifier = Modifier.height(25.dp))
            }
        } // 진화 정보

        /** 진화 정보 없을 시 이미지 **/
        item {
            if (list.isNotEmpty()) return@item

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
        } // 진화 정보 없을 시 이미지

        item { Spacer(modifier = Modifier.height(20.dp)) }
    } // LazyColumn
}

@Composable
fun EvolutionRow(
    evolutionInfo: Evolution,
    isShiny: Boolean = false
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = if (isShiny) evolutionInfo.beforeShinyDot else evolutionInfo.beforeDot,
            contentDescription = "image",
            error = painterResource(id = R.drawable.img_monsterbal),
            placeholder = painterResource(id = R.drawable.img_monsterbal),
            imageLoader = imageLoader,
            modifier = Modifier
                .size(80.dp)
                .weight(1f)
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .size(100.dp)
        ) {
            AsyncImage(
                model = evolutionInfo.evolutionImage,
                contentDescription = "image",
                imageLoader = imageLoader,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = evolutionInfo.evolutionConditions,
                textAlign = TextAlign.Center,
                style = Typography.bodyMedium
            )
        }
        AsyncImage(
            model = if (isShiny) evolutionInfo.afterShinyDot else evolutionInfo.afterDot,
            contentDescription = "image",
            error = painterResource(id = R.drawable.img_monsterbal),
            placeholder = painterResource(id = R.drawable.img_monsterbal),
            imageLoader = imageLoader,
            modifier = Modifier
                .size(80.dp)
                .weight(1f)
        )
    }
}
