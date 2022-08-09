package com.example.pokemondex.repository

import com.example.pokemondex.network.NetworkClient
import com.example.pokemondex.network.data.PokemonItem
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val client: NetworkClient
) {

    suspend fun getPokemonInfo(
        number: String,
        successListener: (PokemonItem) -> Unit,
        failureListener: () -> Unit
    ) {
        client.selectPokemon(
            number = number,
            successListener = {
                it.itemMapper()?.let(successListener) ?: failureListener()
            },
            failureListener = failureListener
        )
    }

}