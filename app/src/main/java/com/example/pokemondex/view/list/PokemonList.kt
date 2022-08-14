package com.example.pokemondex.view.list

import android.os.Build
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.pokemondex.R
import com.example.pokemondex.network.data.PokemonListItem
import com.example.pokemondex.ui.theme.*
import com.example.pokemondex.util.getBlack
import com.example.pokemondex.util.getSkyBlue
import com.example.pokemondex.util.gridItems
import com.example.pokemondex.util.nonRippleClickable
import com.example.pokemondex.view.dialog.LoadingDialog
import com.example.pokemondex.view.navigation.RouteAction

@Composable
fun PokemonListContainer(
    routeAction: RouteAction,
    viewModel: ListViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current
    val loadingState = remember { mutableStateOf(true) }

    val state = viewModel.stateFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() }
    ) {
        /** 상단 타이틀 **/
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_prev),
                contentDescription = "prev",
                colorFilter = ColorFilter.tint(getBlack()),
                modifier = Modifier
                    .padding(all = 17.dp)
                    .nonRippleClickable { routeAction.popupBackStack() }
            )
            Image(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = "menu",
                colorFilter = ColorFilter.tint(getBlack()),
                modifier = Modifier
                    .padding(top = 17.dp, end = 24.dp)
                    .nonRippleClickable {
                        viewModel.event(ListEvent.ImageStateChange)
                    }
            )
        } // 상단 타이틀

        /** 검색창 **/
        SearchTextField(viewModel)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            gridItems(
                data = viewModel.pokemonList,
                columnCount = 4,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) { item ->
                PokemonListItem(
                    item = item,
                    isShiny = viewModel.imageState.value,
                    clickListener = {
                        routeAction.navToDetail(it)
                    }
                )
            }
        }

        when (state.value) {
            ListViewModel.Event.Init -> {
                loadingState.value = true
            }
            ListViewModel.Event.Complete -> {
                loadingState.value = false
            }
        }

        LoadingDialog(loadingState)

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(viewModel: ListViewModel) {
    OutlinedTextField(
        value = viewModel.searchState.value,
        onValueChange = {
            viewModel.event(ListEvent.SearchChange(it))
        },
        shape = RoundedCornerShape(30.dp),
        label = {
            Text(text = "포켓몬 검색", style = Typography.bodyMedium)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = getSkyBlue(),
            cursorColor = getSkyBlue(),
            textColor = White,
            unfocusedLabelColor = Gray,
            focusedLabelColor = Blue,
            focusedBorderColor = Blue,
            unfocusedBorderColor = Blue
        ),
        singleLine = true,
        trailingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "search",
                colorFilter = ColorFilter.tint(getBlack()),
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                }
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )
}

@Composable
fun PokemonListItem(
    item: PokemonListItem,
    isShiny: Boolean,
    clickListener: (String) -> Unit
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

    OutlinedCard(
        border = BorderStroke(width = 1.dp, color = Blue),
        colors = CardDefaults.outlinedCardColors(
            containerColor = getSkyBlue()
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    clickListener(item.number)
                }
        ) {
            AsyncImage(
                model = if (isShiny) item.dotShinyImage else item.dotImage,
                contentDescription = null,
                error = painterResource(id = R.drawable.img_monsterbal),
                placeholder = painterResource(id = R.drawable.img_monsterbal),
                imageLoader = imageLoader,
                modifier = Modifier.size(56.dp)
            )
            Text(
                text = "#${item.number}",
                textAlign = TextAlign.Center,
                color = getBlack(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}