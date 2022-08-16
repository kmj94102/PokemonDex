package com.example.pokemondex.repository

import com.example.pokemondex.network.PokemonClient
import com.example.pokemondex.network.data.PokemonListItem
import com.example.pokemondex.network.data.SearchInfo
import javax.inject.Inject

class ListRepository @Inject constructor(
    private val client: PokemonClient
) {

    suspend fun selectPokemonList(
        generation: String,
        successListener: (List<PokemonListItem>) -> Unit,
        failureListener: () -> Unit
    ) {
        client.selectPokemonList(
            generation = generation,
            successListener = {
                it.mapNotNull { item -> item.mapper() }.let(successListener)
            },
            failureListener = failureListener
        )
    }

    suspend fun selectSearchPokemonList(
        searchInfo: SearchInfo,
        successListener: (List<PokemonListItem>) -> Unit,
        failureListener: () -> Unit
    ) {
        client.selectSearchPokemonList(
            searchInfo = searchInfo,
            successListener = {
                it.mapNotNull { item -> item.mapper() }.let(successListener)
            },
            failureListener = failureListener
        )
    }

}