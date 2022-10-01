package com.example.pokemondex.view.new_dex

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokemondex.R
import com.example.pokemondex.database.CollectionPokemon
import com.example.pokemondex.ui.theme.Blue
import com.example.pokemondex.ui.theme.MainColor
import com.example.pokemondex.ui.theme.SubColor
import com.example.pokemondex.ui.theme.White
import com.example.pokemondex.util.*
import com.example.pokemondex.view.navigation.RouteAction

val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0

@Composable
fun NewDexListScreen(
    routAction: RouteAction,
    viewModel: NewDexViewModel = hiltViewModel()
) {
    val lazyListSate = rememberLazyListState()
    val isOpen = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        NewDexHeader(
            lazyListSate = lazyListSate,
            normalCollect = viewModel.normalCollect.value,
            shinyCollect = viewModel.shinyCollect.value,
            allCollect = viewModel.pokemonList.value.size
        )
        NewDexBody(
            lazyListSate = lazyListSate,
            list = viewModel.pokemonList.value,
            viewModel = viewModel,
            modifier = Modifier.weight(1f)
        )

        NewDexFooter(viewModel, isOpen)

        BackPressHandler {
            if (isOpen.value) {
                isOpen.value = false
            } else {
                routAction.popupBackStack()
            }
        }
    }
}

@Composable
fun NewDexHeader(
    lazyListSate: LazyListState,
    normalCollect: Int,
    shinyCollect: Int,
    allCollect: Int
) {
    val imageHeight = animateDpAsState(targetValue = if (lazyListSate.isScrolled) 0.dp else 172.dp)
    val textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_arceus_logo),
            contentDescription = "logo",
            Modifier
                .size(width = 217.dp, height = imageHeight.value)
                .align(Alignment.CenterHorizontally)
        )

        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_ball_fill),
                contentDescription = "ball"
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "일반 $normalCollect/$allCollect", style = textStyle)

            Spacer(modifier = Modifier.width(10.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_ball_shiny),
                contentDescription = "ball"
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "이로치 $shinyCollect/$allCollect", style = textStyle)
        }
    }
}

@Composable
fun NewDexBody(
    lazyListSate: LazyListState,
    list: List<CollectionPokemon>,
    viewModel: NewDexViewModel,
    modifier: Modifier = Modifier
) {

    LazyColumn(state = lazyListSate, modifier = modifier) {
        gridItems(
            data = list,
            columnCount = 2,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 24.dp)
        ) { item, _ ->
            NewDexListItem(item, viewModel)
        }
    }
}

@Composable
fun NewDexListItem(pokemon: CollectionPokemon, viewModel: NewDexViewModel) {
    val context = LocalContext.current
    val imageLoader = getImageLoader(context)

    val textStyle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = getBlack()
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = getSkyBlue()
        ),
        border = BorderStroke(1.dp, Blue),
        shape = RoundedCornerShape(10.dp)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

            val (image, number, name, normal, shiny, star) = createRefs()

            AsyncImage(
                model = pokemon.dotImage,
                contentDescription = null,
                error = painterResource(id = R.drawable.img_monsterbal),
                placeholder = painterResource(id = R.drawable.img_monsterbal),
                imageLoader = imageLoader,
                modifier = Modifier
                    .size(56.dp)
                    .constrainAs(image) {
                        top.linkTo(parent.top, 4.dp)
                        start.linkTo(parent.start, 7.dp)
                        bottom.linkTo(parent.bottom, 4.dp)
                    }
            )

            Text(
                text = pokemon.number,
                style = textStyle,
                fontSize = 12.sp,
                modifier = Modifier.constrainAs(number) {
                    top.linkTo(parent.top, 5.dp)
                    start.linkTo(image.end, 2.dp)
                })

            ScrolledText(
                text = pokemon.name,
                textStyle = textStyle,
                modifier = Modifier
                    .constrainAs(name) {
                        top.linkTo(number.bottom)
                        start.linkTo(number.start)
                        end.linkTo(parent.end, 5.dp)
                        width = Dimension.fillToConstraints
                    }
            )

            Image(
                painter = painterResource(id = if (pokemon.normal) R.drawable.ic_ball_fill else R.drawable.ic_ball),
                contentDescription = "normal",
                modifier = Modifier
                    .nonRippleClickable {
                        viewModel.event(
                            NewDexEvent.Update(
                                group = NewDexViewModel.Normal,
                                number = pokemon.number,
                                isChecked = pokemon.normal.not()
                            )
                        )
                    }
                    .constrainAs(normal) {
                        top.linkTo(name.bottom)
                        start.linkTo(name.start)
                        bottom.linkTo(parent.bottom, 5.dp)
                    }
            )

            Image(
                painter = painterResource(id = if (pokemon.shiny) R.drawable.ic_ball_shiny else R.drawable.ic_ball),
                contentDescription = "shiny",
                modifier = Modifier
                    .nonRippleClickable {
                        viewModel.event(
                            NewDexEvent.Update(
                                group = NewDexViewModel.Shiny,
                                number = pokemon.number,
                                isChecked = pokemon.shiny.not()
                            )
                        )
                    }
                    .constrainAs(shiny) {
                        top.linkTo(normal.top)
                        start.linkTo(normal.end, 3.dp)
                    }
            )

            Image(
                painter = painterResource(id = if (pokemon.importance) R.drawable.ic_star_fill else R.drawable.ic_star),
                contentDescription = "star",
                modifier = Modifier
                    .nonRippleClickable {
                        viewModel.event(
                            NewDexEvent.Update(
                                group = NewDexViewModel.Importance,
                                number = pokemon.number,
                                isChecked = pokemon.importance.not()
                            )
                        )
                    }
                    .constrainAs(star) {
                        top.linkTo(shiny.top)
                        start.linkTo(shiny.end, 3.dp)
                    }
            )

        }
    }
}

@Composable
fun ScrolledText(
    text: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var shouldAnimate by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = shouldAnimate) {
        scrollState.animateScrollTo(
            scrollState.maxValue,
            animationSpec = tween(1000, 200, easing = CubicBezierEasing(0f, 0f, 0f, 0f))
        )
        scrollState.animateScrollTo(
            0,
            animationSpec = tween(1000, 200, easing = CubicBezierEasing(0f, 0f, 0f, 0f))
        )
        shouldAnimate = !shouldAnimate
    }

    Text(
        text = text,
        style = textStyle,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .horizontalScroll(scrollState, false)
    )
}

@Composable
fun NewDexFooter(viewModel: NewDexViewModel, isOpen: MutableState<Boolean>) {
    val radioSelected = remember { mutableStateOf("전체") }
    val valueState = remember { mutableStateOf("") }

    val rotateValue by animateFloatAsState(targetValue = if (isOpen.value) 0f else 180f)

    val radioItems = listOf("전체", "미포획", "즐겨찾기")
    val isSelectedItem: (String) -> Boolean = { radioSelected.value == it }
    val onSelectedChange: (String) -> Unit = {
        when(it) {
            radioItems[0] -> viewModel.event(NewDexEvent.SelectInfo(NewDexViewModel.All))
            radioItems[1] -> viewModel.event(NewDexEvent.SelectInfo(NewDexViewModel.NonCollection))
            radioItems[2] -> viewModel.event(NewDexEvent.SelectInfo(NewDexViewModel.Importance))
        }
        radioSelected.value = it
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MainColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .clipToBounds()
                .clickable { isOpen.value = isOpen.value.not() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = "arrow",
                tint = White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(45.dp)
                    .rotate(rotateValue)
            )
        }

        AnimatedVisibility(visible = isOpen.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SubColor)
            ) {
                SearchTextField(
                    value = valueState.value,
                    onValueChange = { valueState.value = it },
                    onSearch = {
                        viewModel.event(NewDexEvent.Search(valueState.value))
                    },
                    modifier = Modifier.padding(0.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    radioItems.forEach {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = isSelectedItem(it),
                                onClick = { onSelectedChange(it) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Blue,
                                    unselectedColor = White
                                )
                            )
                            Text(
                                text = it,
                                style = TextStyle(fontSize = 14.sp, color = White),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.nonRippleClickable {
                                    onSelectedChange(it)
                                }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }

            }
        }
    }
}