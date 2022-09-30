package com.example.pokemondex.view.download

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokemondex.R
import com.example.pokemondex.util.Constants
import com.example.pokemondex.util.Title
import com.example.pokemondex.view.add.CustomButton
import com.example.pokemondex.view.dialog.ConfirmDialog
import com.example.pokemondex.view.dialog.LoadingDialog
import com.example.pokemondex.view.dialog.SelectInfoDialog
import com.example.pokemondex.view.navigation.RouteAction

@Composable
fun DownloadScreen(
    routeAction: RouteAction,
    viewModel: DownloadViewModel = hiltViewModel()
) {

    val isLoading = remember { mutableStateOf(false) }
    val isSuccess = remember { mutableStateOf(false) }
    val isFail = remember { mutableStateOf(false) }
    val eventState = viewModel.eventFlow.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Title(title = "다운로드", routeAction = routeAction)
        DownloadBody(viewModel)
    }

    LoadingDialog(isLoading = isLoading)
    ConfirmDialog(message = "성공", isShow = isSuccess) {
        isSuccess.value = false
    }

    when (eventState.value) {
        DownloadViewModel.Event.Init -> {
            isLoading.value = false
        }
        DownloadViewModel.Event.Loading -> {
            isLoading.value = true
        }
        DownloadViewModel.Event.Error -> {
            isLoading.value = false
            isSuccess.value = true
        }
        DownloadViewModel.Event.Success -> {
            isLoading.value = false
            isFail.value = true
        }
    }

}

@Composable
fun DownloadBody(viewModel: DownloadViewModel) {
    val isShow = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var group = Constants.Pokemon

    Column(
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {

        CustomButton(value = "포켓몬 추가") {
            group = Constants.Pokemon
            isShow.value = true
        }

        CustomButton(value = "진화 추가") {
            group = Constants.Pokemon
            isShow.value = true
        }

        CustomButton(value = "진화 타입") {
        }

    }

    SelectInfoDialog(isShow = isShow, context = context) { start, end ->
        isShow.value = false
        if (group == Constants.Pokemon) {
            viewModel.event(DownloadEvent.NewPokemonInsert(start, end))
        } else {
            viewModel.event(DownloadEvent.EvolutionInsert(start, end))
        }
    }
}