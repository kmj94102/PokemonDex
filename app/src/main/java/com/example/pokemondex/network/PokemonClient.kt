package com.example.pokemondex.network

import com.example.pokemondex.network.data.Characteristic
import com.example.pokemondex.network.data.Pokemon
import com.example.pokemondex.network.data.PokemonListItemTemp
import com.example.pokemondex.network.data.PokemonResult
import javax.inject.Inject

class PokemonClient @Inject constructor(
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

    suspend fun selectPokemonList(
        successListener: (List<PokemonListItemTemp>) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.getPokemonList())
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun selectPokemon(
        number: String,
        successListener: (PokemonResult) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.getPokemon(number))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}