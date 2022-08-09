package com.example.pokemondex.repository

import com.example.pokemondex.network.NetworkClient
import com.example.pokemondex.network.data.PokemonListItem
import javax.inject.Inject

class ListRepository @Inject constructor(
    private val client: NetworkClient
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