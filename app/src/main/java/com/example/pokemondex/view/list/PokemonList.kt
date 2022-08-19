package com.example.pokemondex.view.list

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.pokemondex.R
import com.example.pokemondex.network.data.ConditionGenerate
import com.example.pokemondex.network.data.PokemonListItem
import com.example.pokemondex.ui.theme.*
import com.example.pokemondex.util.*
import com.example.pokemondex.view.dialog.LoadingDialog
import com.example.pokemondex.view.navigation.RouteAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PokemonListContainer(
    routeAction: RouteAction,
    viewModel: ListViewModel = hiltViewModel()
) {

    val focusManager = LocalFocusManager.current
    val loadingState = remember { mutableStateOf(true) }

    val state = viewModel.stateFlow.collectAsState()
    val drawerState = androidx.compose.material.rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        /** 메뉴 **/
        drawerContent = {
            MenuBody(scope, drawerState, viewModel)
        }, // 메뉴
        /** 포켓몬 리스트 **/
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { focusManager.clearFocus() }
            ) {
                /** 상단 타이틀 **/
                PokemonListHeader(routeAction, viewModel, scope, drawerState)

                /** 검색창, 포켓몬 리스트 **/
                PokemonListBody(routeAction, viewModel)

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
        } // 포켓몬 리스트
    )


}

@Composable
fun PokemonListHeader(
    routeAction: RouteAction,
    viewModel: ListViewModel,
    scope: CoroutineScope,
    drawerState: DrawerState,
) {
    Row(
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

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = if (viewModel.imageState.value) R.drawable.ic_shiny else R.drawable.ic_none_shiny),
            contentDescription = "shiny",
            modifier = Modifier.padding(top = 17.dp, end = 10.dp)
                .size(24.dp)
                .nonRippleClickable {
                    viewModel.event(ListEvent.ImageStateChange)
                }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_menu),
            contentDescription = "menu",
            colorFilter = ColorFilter.tint(getBlack()),
            modifier = Modifier
                .padding(top = 17.dp, end = 24.dp)
                .nonRippleClickable {
                    scope.launch {
                        viewModel.event(ListEvent.MenuOpen)
                        drawerState.open()
                    }
                }
        )
    }
}

@Composable
fun PokemonListBody(
    routeAction: RouteAction,
    viewModel: ListViewModel
) {
    /** 검색창 **/
    SearchTextField(viewModel)

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
                isShiny = viewModel.imageState.value,
                clickListener = {
                    routeAction.navToDetail(it)
                }
            )
        }
    } // 포켓몬 리스트
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(viewModel: ListViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = viewModel.searchState.value,
        onValueChange = {
            viewModel.event(ListEvent.SearchTextFieldChange(it))
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
                        viewModel.event(ListEvent.Search)
                    }
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                viewModel.event(ListEvent.Search)
                keyboardController?.hide()
            }
        ),
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
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

@Composable
fun MenuBody(
    scope: CoroutineScope,
    drawerState: DrawerState,
    viewModel: ListViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(getWhite())
    ) {

        /** 타이틀 **/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "조건",
                fontSize = 36.sp,
                style = Typography.titleLarge,
                color = MainColor
            )
        } // 타이틀

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            /** 세대 **/
            item {
                Text(
                    text = "세대",
                    style = Typography.bodyLarge,
                    color = getBlack(),
                    modifier = Modifier.padding(start = 16.dp, bottom = 10.dp)
                )
            }

            /** 전체 세대 버튼 **/
            item {
                val isAllSelect = viewModel.generateList.map { it.isSelect }.none { it.not() }
                CustomRadioButton(
                    isSelected = isAllSelect,
                    selectText = "세대 전체 해제",
                    unselectText = "세대 전체 선택",
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    viewModel.event(ListEvent.GenerateCondition(-1))
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            /** 각 세대 선택 **/
            item {
                val list = viewModel.generateList
                GenerateButtons(list, viewModel)
            }

            item { Spacer(modifier = Modifier.height(10.dp)) }

            /** 타입 **/
            item {
                Text(
                    text = "타입",
                    style = Typography.bodyLarge,
                    color = getBlack(),
                    modifier = Modifier.padding(start = 16.dp, bottom = 10.dp)
                )
            }

            /** 전체 타입 버튼 **/
            item {
                val isAllSelect = viewModel.typeList.map { it.isSelect }.none { it.not() }
                CustomRadioButton(
                    isSelected = isAllSelect,
                    selectText = "타입 전체 해제",
                    unselectText = "타입 전체 선택",
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    viewModel.event(ListEvent.TypeCondition(-1))
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            /** 각 타입 선택 **/
            gridItems(
                data = viewModel.typeList,
                columnCount = 5,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) { data, index ->
                Image(
                    painter = painterResource(id = data.imageRes),
                    contentDescription = "type",
                    alpha = if (data.isSelect) 1f else 0.3f,
                    modifier = Modifier.nonRippleClickable {
                        viewModel.event(ListEvent.TypeCondition(index))
                    }
                )
            }
        }

        /** 적용 버튼 **/
        Button(
            onClick = {
                scope.launch {
                    viewModel.event(ListEvent.SettingCondition)
                    drawerState.close()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MainColor
            ),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "적용",
                style = Typography.bodyLarge,
                color = White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

    }
}

@Composable
fun CustomRadioButton(
    isSelected: Boolean,
    selectText: String,
    unselectText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = { onClick() },
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) {
                Yellow
            } else {
                Color.Transparent
            }
        ),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(1.dp, MainColor),
        contentPadding = PaddingValues(vertical = 2.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = if (isSelected) selectText else unselectText,
            style = Typography.bodyLarge,
            color = MainColor
        )
    }
}

@Composable
fun GenerateRadioButton(
    generate: ConditionGenerate,
    modifier: Modifier,
    onClick: () -> Unit
) {
    CustomRadioButton(
        isSelected = generate.isSelect,
        selectText = generate.generate,
        unselectText = generate.generate,
        modifier = modifier
    ) {
        onClick()
    }
}

@Composable
fun GenerateButtons(list: List<ConditionGenerate>, viewModel: ListViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            (0..2).forEach {
                GenerateRadioButton(
                    generate = list[it],
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                ) {
                    viewModel.event(ListEvent.GenerateCondition(it))
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            (3..5).forEach {
                GenerateRadioButton(
                    generate = list[it],
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                ) {
                    viewModel.event(ListEvent.GenerateCondition(it))
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            (6..8).forEach {
                GenerateRadioButton(
                    generate = list[it],
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp)
                ) {
                    viewModel.event(ListEvent.GenerateCondition(it))
                }
            }
        }
    }
}