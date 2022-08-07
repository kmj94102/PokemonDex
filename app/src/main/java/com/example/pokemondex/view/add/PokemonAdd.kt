package com.example.pokemondex.view.add

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PokemonAddContainer() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        Text(text = "포켓몬 등록")
    }
}