package com.example.pokemondex.repository

import com.example.pokemondex.network.PokemonClient
import com.example.pokemondex.network.data.PokemonListItem
import javax.inject.Inject

class ListRepository @Inject constructor(
    private val client: PokemonClient
) {

    suspend fun selectPokemonList(
        successListener: (List<PokemonListItem>) -> Unit,
        failureListener: () -> Unit
    ) {
        client.selectPokemonList(
            successListener = {
                it.mapNotNull { item -> item.mapper() }.let(successListener)
            },
            failureListener = failureListener
        )
    }

}