package com.example.pokemondex.view.detail

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.pokemondex.R
import com.example.pokemondex.view.navigation.RouteAction

@Composable
fun DetailContainer(
    routeAction: RouteAction,
    viewModel: DetailViewModel = hiltViewModel()
) {

    val info = viewModel.pokemonInfo.value
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

    Column(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = info.image,
            contentDescription = "이미지",
            error = painterResource(id = R.drawable.img_monsterbal),
            placeholder = painterResource(id = R.drawable.img_monsterbal),
            imageLoader = imageLoader,
            modifier = Modifier.size(100.dp)
        )
        AsyncImage(
            model = info.shinyImage,
            contentDescription = "이미지",
            error = painterResource(id = R.drawable.img_monsterbal),
            placeholder = painterResource(id = R.drawable.img_monsterbal),
            imageLoader = imageLoader,
            modifier = Modifier.size(100.dp)
        )
    }
}