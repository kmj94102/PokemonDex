package com.example.pokemondex.network

import com.example.pokemondex.network.data.Characteristic
import com.example.pokemondex.network.data.Pokemon
import javax.inject.Inject

class NetworkClient @Inject constructor(
    private val service: PokemonService
){

    suspend fun insertPokemon(
        pokemon: Pokemon,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.insertPokemonInfo(pokemon))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun insertCharacteristic(
        characteristic: Characteristic,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.insertCharacteristic(characteristic))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}