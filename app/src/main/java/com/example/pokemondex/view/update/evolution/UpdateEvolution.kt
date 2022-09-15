package com.example.pokemondex.view.update.evolution

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokemondex.R
import com.example.pokemondex.network.data.EvolutionTemp
import com.example.pokemondex.ui.theme.MainColor
import com.example.pokemondex.ui.theme.Typography
import com.example.pokemondex.ui.theme.White
import com.example.pokemondex.util.getBlack
import com.example.pokemondex.util.nonRippleClickable
import com.example.pokemondex.util.toast
import com.example.pokemondex.view.add.*
import com.example.pokemondex.view.dialog.LoadingDialog
import com.example.pokemondex.view.dialog.RegisterResultDialog
import com.example.pokemondex.view.navigation.RouteAction
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UpdateEvolutionContainer(
    routeAction: RouteAction,
    viewModel: UpdateEvolutionViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val dialogState = remember { mutableStateOf(false) }
    val resultDialogState = remember { mutableStateOf(false) }
    val resultMessage = remember { mutableStateOf("") }

    LazyColumn(contentPadding = PaddingValues(bottom = 30.dp), modifier = Modifier.fillMaxSize()) {
        /** 상단 타이틀 **/
        item { UpdateTitle(routeAction) }

        item {
            viewModel.evolutionState.forEachIndexed { index, evolutionTemp ->
                UpdateEvolutionItem(index, evolutionTemp, viewModel)
            }
        }

        /** 추가/삭제 버튼 **/
        item {
            CustomButton(
                value = "추가",
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                viewModel.event(UpdateEvolutionEvent.ListAdd)
            }

            Spacer(modifier = Modifier.height(10.dp))

            CustomButton(
                value = "삭제",
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                viewModel.event(UpdateEvolutionEvent.ListRemove)
            }
        }

        /** 수정하기 버튼 **/
        item {
            Spacer(modifier = Modifier.height(10.dp))

            CustomButton(
                value = "수정하기",
                color = MainColor,
                textColor = White,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                viewModel.event(
                    UpdateEvolutionEvent.UpdateEvolution {
                        resultDialogState.value = true
                        resultMessage.value = it
                    }
                )
            }
        }

        when (state.value) {
            UpdateEvolutionViewModel.Event.Init -> {}
            UpdateEvolutionViewModel.Event.Loading -> {
                scope.launch {
                    delay(1500)
                    if (state.value == UpdateEvolutionViewModel.Event.Loading) {
                        dialogState.value = true
                    }
                }
            }
            UpdateEvolutionViewModel.Event.Complete -> {
                if (viewModel.evolutionState.isEmpty()) {
                    item {
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
    }

    RegisterResultDialog(
        result = resultMessage.value,
        isShow = resultDialogState
    ) {
        resultDialogState.value = false
        routeAction.popupBackStack()
    }

    LoadingDialog(isLoading = dialogState)

}

@Composable
fun UpdateTitle(routeAction: RouteAction) {
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
    } // Box
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UpdateEvolutionItem(
    index: Int,
    info: EvolutionTemp,
    viewModel: UpdateEvolutionViewModel
) {

    val context = LocalContext.current
    val isExpanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        /** 진화 전/후 번호 **/
        Row(modifier = Modifier.fillMaxWidth()) {
            /** 진화 전 **/
            CustomTextFiled(
                value = info.beforeNum,
                hint = "진화 전",
                changeListener = {
                    viewModel.event(
                        UpdateEvolutionEvent.InputTextChange(
                            code = AddEvolutionViewModel.Before,
                            index = index,
                            value = it
                        )
                    )
                },
                inputType = KeyboardType.Number,
                modifier = Modifier
                    .weight(1f)
            ) // 진화 전

            Spacer(modifier = Modifier.width(10.dp))

            /** 진화 후 **/
            CustomTextFiled(
                value = info.afterNum,
                hint = "진화 후",
                changeListener = {
                    viewModel.event(
                        UpdateEvolutionEvent.InputTextChange(
                            code = AddEvolutionViewModel.After,
                            index = index,
                            value = it
                        )
                    )
                },
                inputType = KeyboardType.Number,
                modifier = Modifier
                    .weight(1f)
            ) // 진화 후
        } // Row

        Spacer(modifier = Modifier.height(10.dp))

        /** 진화 타입 **/
        ExposedDropdownMenuBox(
            expanded = isExpanded.value,
            onExpandedChange = {
                isExpanded.value = it
            }
        ) {
            CustomTextFiled(
                value = info.evolutionType,
                hint = "진화 타입",
                changeListener = {},
                modifier = Modifier
                    .fillMaxWidth()
            )

            TypeDropDownMenu(
                isExpanded = isExpanded,
                viewModel.evolutionTypes
            ) {
                viewModel.event(
                    UpdateEvolutionEvent.InputTextChange(
                        index = index,
                        code = AddEvolutionViewModel.Type,
                        value = it
                    )
                )

                viewModel.event(
                    UpdateEvolutionEvent.InputTextChange(
                        index = index,
                        code = AddEvolutionViewModel.Conditions,
                        value = readyLetters(it)
                    )
                )
            }
        } // 진화 타입

        Spacer(modifier = Modifier.height(10.dp))

        /** 진화 방법 **/
        CustomTextFiled(
            value = info.evolutionConditions,
            hint = "진화 방법",
            changeListener = {
                viewModel.event(
                    UpdateEvolutionEvent.InputTextChange(
                        index = index,
                        code = AddEvolutionViewModel.Conditions,
                        value = it
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
        ) // 진화 방법

        Spacer(modifier = Modifier.height(10.dp))

        /** 등록하고자 하는 포켓몬 이미지 조회 : 확인용 **/
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = info.beforeImage,
                contentDescription = "",
                error = painterResource(id = R.drawable.img_monsterbal),
                placeholder = painterResource(id = R.drawable.img_monsterbal),
                modifier = Modifier.size(50.dp)
            )

            AsyncImage(
                model = info.afterImage,
                contentDescription = "",
                error = painterResource(id = R.drawable.img_monsterbal),
                placeholder = painterResource(id = R.drawable.img_monsterbal),
                modifier = Modifier.size(50.dp)
            )

            /** 이미지 조회 **/
            CustomButton(
                value = "조회",
                modifier = Modifier.width(100.dp)
            ) {
                viewModel.event(
                    UpdateEvolutionEvent.ImageSelect(
                        index = index,
                        failureListener = {
                            context.toast(it)
                        }
                    )
                )
            }
        } // Row

        Spacer(modifier = Modifier.height(10.dp))

    }
}