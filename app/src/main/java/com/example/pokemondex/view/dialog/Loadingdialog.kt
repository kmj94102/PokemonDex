package com.example.pokemondex.view.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.*
import com.example.pokemondex.R

@Composable
fun LoadingDialog(isLoading: State<Boolean>) {
    if (isLoading.value) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.pokemon_loading)
        )
        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = true,
            iterations = LottieConstants.IterateForever,
        )

        Dialog(onDismissRequest = {}) {

            Box(modifier = Modifier.fillMaxSize()) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )

            }

        }
    }
}