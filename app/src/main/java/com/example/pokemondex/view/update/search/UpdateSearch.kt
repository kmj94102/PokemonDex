package com.example.pokemondex.view.update.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokemondex.R
import com.example.pokemondex.ui.theme.Blue
import com.example.pokemondex.ui.theme.Gray
import com.example.pokemondex.ui.theme.MainColor
import com.example.pokemondex.ui.theme.Typography
import com.example.pokemondex.util.getBlack
import com.example.pokemondex.util.getSkyBlue
import com.example.pokemondex.util.gridItems
import com.example.pokemondex.util.nonRippleClickable
import com.example.pokemondex.view.dialog.LoadingDialog
import com.example.pokemondex.view.list.PokemonListItem
import com.example.pokemondex.view.navigation.RouteAction
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UpdateSearchContainer(
    routeAction: RouteAction,
    type: String,
    viewModel: UpdateSearchViewModel = hiltViewModel()
) {

    val stateFlow = viewModel.eventStateFlow.collectAsState()
    val isLoading = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        /** 타이틀 **/
        UpdateTitle(routeAction)
        /** 검색창 **/
        SearchTextField(viewModel)

        Spacer(modifier = Modifier.height(10.dp))

        SearchResults(viewModel, routeAction, type)

        when (stateFlow.value) {
            UpdateSearchViewModel.Event.Init -> {
                SearchGuide()
            }
            UpdateSearchViewModel.Event.Loading -> {
                rememberCoroutineScope().launch {
                    delay(1500)
                    if (stateFlow.value == UpdateSearchViewModel.Event.Loading) {
                        isLoading.value = true
                    }
                }
            }
            UpdateSearchViewModel.Event.Complete -> {
                isLoading.value = false
                if (viewModel.pokemonList.isEmpty()) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_empty4),
                            contentDescription = "empty",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = stringResource(id = R.string.load_error),
                            color = getBlack(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        LoadingDialog(isLoading)
    }

}

@Composable
fun UpdateTitle(routeAction: RouteAction) {
    /** 상단 타이틀 **/
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 17.dp, horizontal = 17.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_prev),
            contentDescription = "prev",
            colorFilter = ColorFilter.tint(getBlack()),
            modifier = Modifier
                .nonRippleClickable {
                    routeAction.popupBackStack()
                }
        )
        Text(
            text = "포켓몬 진화 수정",
            style = Typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MainColor,
            modifier = Modifier.align(Alignment.Center)
        )
    } // 상단 타이틀
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(viewModel: UpdateSearchViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = viewModel.searchState.value,
        onValueChange = {
            viewModel.event(UpdateSearchEvent.SearchTextFieldChange(it))
        },
        shape = RoundedCornerShape(30.dp),
        label = {
            Text(text = stringResource(id = R.string.search), style = Typography.bodyMedium)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = getSkyBlue(),
            cursorColor = getSkyBlue(),
            textColor = getBlack(),
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
                modifier = Modifier
                    .padding(end = 20.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        viewModel.event(UpdateSearchEvent.Search)
                    }
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                viewModel.event(UpdateSearchEvent.Search)
                keyboardController?.hide()
            }
        ),
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
    )
}

@Composable
fun SearchGuide() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_guide),
            contentDescription = "guide",
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "변경할 포켓몬을 검색해주세요",
            color = getBlack(),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SearchResults(
    viewModel: UpdateSearchViewModel,
    routeAction: RouteAction,
    type: String
) {
    /** 포켓몬 리스트 **/
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        gridItems(
            data = viewModel.pokemonList,
            columnCount = 4,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) { item, _ ->
            PokemonListItem(
                item = item,
                isShiny = false,
                clickListener = {
                    when(type) {
                        RouteAction.UpdateEvolution -> {
                            routeAction.navToUpdateEvolution(it)
                        }
                        RouteAction.NewPokemonDex -> {
                            routeAction.navToNewPokemonDex(it)
                        }
                    }
                }
            )
        }
    } // 포켓몬 리스트
}