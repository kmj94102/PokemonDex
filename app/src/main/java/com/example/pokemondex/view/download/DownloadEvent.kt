package com.example.pokemondex.view.download

sealed class DownloadEvent {
    data class NewPokemonInsert(
        val startIndex: String,
        val endIndex: String
    ) : DownloadEvent()
    data class EvolutionInsert(
        val startIndex: String,
        val endIndex: String
    ) : DownloadEvent()
}
