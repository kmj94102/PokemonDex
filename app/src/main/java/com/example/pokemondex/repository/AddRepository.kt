package com.example.pokemondex.repository

import com.example.pokemondex.network.NetworkClient
import com.example.pokemondex.network.data.Characteristic
import com.example.pokemondex.network.data.Pokemon
import com.example.pokemondex.network.data.PokemonListItem
import javax.inject.Inject

class AddRepository @Inject constructor(
    private val client: NetworkClient
) {

    suspend fun insertPokemon(
        pokemon: Pokemon,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) {
        client.insertPokemon(
            pokemon = pokemon,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    suspend fun insertCharacteristic(
        characteristic: Characteristic,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) {
        client.insertCharacteristic(
            characteristic = characteristic,
            successListener = successListener,
            failureListener = failureListener
        )
    }

}