package com.example.pokemondex.view.add

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pokemondex.R
import com.example.pokemondex.network.data.EvolutionTemp
import com.example.pokemondex.ui.theme.*
import com.example.pokemondex.util.*
import com.example.pokemondex.view.dialog.RegisterResultDialog
import com.example.pokemondex.view.navigation.RouteAction

@Composable
fun AddEvolutionContainer(
    routeAction: RouteAction,
    viewModel: AddEvolutionViewModel = hiltViewModel()
) {

    val dialogState = remember { mutableStateOf(false) }
    var resultState by remember { mutableStateOf("") }

    LazyColumn(modifier = Modifier.padding(bottom = 40.dp)) {
        /** 상단 타이틀 **/
        item {
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
                androidx.compose.material3.Text(
                    text = "포켓몬 진화 등록",
                    style = Typography.titleLarge,
                    textAlign = TextAlign.Center,
                    color = MainColor,
                    modifier = Modifier.align(Alignment.Center)
                )
            } // Box
        } // 상단 타이틀

        item { Spacer(modifier = Modifier.height(10.dp)) }

        /** 진화 정보 **/
        item {
            viewModel.evolutionInfoList.forEachIndexed { index, evolutionTemp ->
                AddEvolutionItem(index, evolutionTemp, viewModel)
            }
        } // 진화 정보

        item { Spacer(modifier = Modifier.height(10.dp)) }

        /** 추가 **/
        item {
            CustomButton(
                value = "추가",
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                viewModel.event(AddEvolutionEvent.ListAdd)
            }
        } // 추가

        item { Spacer(modifier = Modifier.height(10.dp)) }

        /** 삭제 **/
        item {
            CustomButton(
                value = "삭제",
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                viewModel.event(AddEvolutionEvent.ListRemove)
            }
        } // 삭제

        item { Spacer(modifier = Modifier.height(30.dp)) }

        item {
            Button(
                onClick = {
                    viewModel.event(AddEvolutionEvent.InsertEvolution {
                        resultState = it
                        dialogState.value = true
                    })
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MainColor
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "등록",
                    color = White,
                    style = Typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    } // Lazy Column

    RegisterResultDialog(
        result = resultState,
        isShow = dialogState
    ) {
        dialogState.value = false
    }

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddEvolutionItem(
    index: Int,
    info: EvolutionTemp,
    viewModel: AddEvolutionViewModel
) {

    val context = LocalContext.current
    val isExpanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        /** 진화 전 **/
        CustomTextFiled(
            value = info.beforeNum,
            hint = "진화 전",
            changeListener = {
                viewModel.event(
                    AddEvolutionEvent.InputTextChange(
                        index = index,
                        code = AddEvolutionViewModel.Before,
                        value = it
                    )
                )
            },
            inputType = KeyboardType.Number,
            modifier = Modifier
                .fillMaxWidth()
        ) // 진화 전

        Spacer(modifier = Modifier.height(10.dp))

        /** 진화 후 **/
        CustomTextFiled(
            value = info.afterNum,
            hint = "진화 후",
            changeListener = {
                viewModel.event(
                    AddEvolutionEvent.InputTextChange(
                        index = index,
                        code = AddEvolutionViewModel.After,
                        value = it
                    )
                )
            },
            inputType = KeyboardType.Number,
            modifier = Modifier
                .fillMaxWidth()
        ) // 진화 후

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
                    AddEvolutionEvent.InputTextChange(
                        index = index,
                        code = AddEvolutionViewModel.Type,
                        value = it
                    )
                )

                viewModel.event(
                    AddEvolutionEvent.InputTextChange(
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
                    AddEvolutionEvent.InputTextChange(
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
                    AddEvolutionEvent.ImageSelect(
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

@Composable
fun TypeDropDownMenu(
    isExpanded: MutableState<Boolean>,
    list: List<String>,
    selectListener: (String) -> Unit
) {
    DropdownMenu(
        expanded = isExpanded.value,
        onDismissRequest = { isExpanded.value = false },
        modifier = Modifier
            .background(getWhite())
    ) {
        list.forEach {
            DropdownMenuItem(
                onClick = {
                    selectListener(it)
                    isExpanded.value = false
                }
            ) {
                Text(text = it, color = getBlack())
            }
        }

    }
}

private fun readyLetters(type: String) =
    when(type) {
        "이상한사탕" -> "Lv."
        "친밀도" -> "친밀도 0 이상 레벨업"
        "다이맥스" -> "거다이맥스"
        "메가진화" -> "메가진화"
        else -> type
    }