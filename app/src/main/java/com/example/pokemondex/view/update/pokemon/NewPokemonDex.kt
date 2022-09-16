package com.example.pokemondex.view.update.pokemon

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokemondex.util.Title
import com.example.pokemondex.util.toast
import com.example.pokemondex.view.add.CustomButton
import com.example.pokemondex.view.add.CustomTextFiled
import com.example.pokemondex.view.navigation.RouteAction

@Composable
fun NewPokemonDexContainer(
    routeAction: RouteAction,
    viewModel: NewDexViewModel = hiltViewModel()
) {

    val info = viewModel.info.value
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        /** 상단 타이틀 **/
        Title(title = "신도감 등록", routeAction = routeAction)

        CustomTextFiled(
            value = info.number,
            hint = "도감 번호",
            inputType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            changeListener = {
                viewModel.event(NewDexEvent.DexNumber(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        CustomButton(
            value = "등록하기",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            viewModel.event(NewDexEvent.Complete(
                successListener = {
                    context.toast("등록이 완료되었습니다.")
                    routeAction.popupBackStack()
                },
                failureListener = {
                    context.toast("등록 실패.")
                }
            ))
        }

    }

}