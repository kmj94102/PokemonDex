package com.example.pokemondex.repository

import com.example.pokemondex.network.ExternalClient
import com.example.pokemondex.network.PokemonClient
import com.example.pokemondex.network.data.*
import javax.inject.Inject

class AddRepository @Inject constructor(
    private val client: PokemonClient,
    private val externalClient: ExternalClient
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

    suspend fun getPokemonInfo(
        index: Int,
        successListener: (PokemonInfoResult, SpeciesInfoResult) -> Unit,
        failureListener: () -> Unit
    ) {
        var info = PokemonInfoResult(listOf(), mapOf(), listOf())
        var speciesInfo = SpeciesInfoResult("","","")
        externalClient.getPokemonInfo(
            index = index,
            successListener = {
                info = it.mapper()
            },
            failureListener = failureListener
        )

        externalClient.getPokemonSpeciesInfo(
            index = index,
            successListener = {
                it.mapper()
                    ?.let { result -> speciesInfo = result }
                    ?: failureListener()
            },
            failureListener = failureListener
        )
        successListener(info, speciesInfo)
    }

    suspend fun getAbilityInfo(
        name: String,
        successListener: (AbilityInfoResult) -> Unit,
        failureListener: () -> Unit
    ) {
        externalClient.getAbilityInfo(
            name = name,
            successListener = {
                it.mapper()?.let(successListener)?:failureListener()
            },
            failureListener = failureListener
        )
    }

}