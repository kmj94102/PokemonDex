package com.example.pokemondex.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemondex.R
import com.example.pokemondex.ui.theme.MainColor
import com.example.pokemondex.ui.theme.Typography
import com.example.pokemondex.util.Constants.generationList
import com.example.pokemondex.util.getWhite
import com.example.pokemondex.util.gridItems
import com.example.pokemondex.view.navigation.RouteAction

@Composable
fun HomeContainer(routeAction: RouteAction) {

    val list = generationList

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                text = stringResource(id = R.string.pokedex),
                style = Typography.titleLarge,
                fontSize = 36.sp,
                color = MainColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp)
            )
        }

        item { Spacer(modifier = Modifier.height(25.dp)) }

        gridItems(
            data = list,
            columnCount = 2,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) { itemData ->
            HomeCardButton(itemData) {
                routeAction.navToList(it.replace("세대", ""))
            }
        }

        item {
            Button(onClick = { routeAction.navToAdd() }) {
                Text(text = "포켓몬 등록")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCardButton(
    data: Pair<String, Int>,
    modifier: Modifier = Modifier,
    clickListener: (String) -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = getWhite(),
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        onClick = {
            clickListener(data.first)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(102.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = data.first,
                fontSize = 20.sp,
                style = Typography.bodyLarge,
                color = MainColor,
                modifier = Modifier.padding(13.dp)
            )
            Image(
                painter = painterResource(id = data.second),
                contentDescription = "image",
                modifier = Modifier
                    .width(151.dp)
                    .height(102.dp)
                    .align(Alignment.BottomEnd)
            )
        }
    }
}