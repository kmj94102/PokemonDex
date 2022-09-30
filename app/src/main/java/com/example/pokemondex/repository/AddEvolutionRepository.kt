package com.example.pokemondex.repository

import com.example.pokemondex.network.PokemonClient
import com.example.pokemondex.network.data.EvolutionInfo
import com.example.pokemondex.network.data.PokemonListItem
import javax.inject.Inject

class AddEvolutionRepository @Inject constructor(
    private val client: PokemonClient
) {

    suspend fun getPokemonImage(
        number: String,
        successListener: (PokemonListItem) -> Unit,
        failureListener: () -> Unit
    ) {
        client.selectPokemonImage(
            number = number,
            successListener = {
                it.mapper()?.let(successListener) ?: failureListener()
            },
            failureListener = failureListener
        )
    }

    suspend fun getEvolutionType(
        successListener: (List<String>) -> Unit,
        failureListener: () -> Unit
    ) {
        client.selectEvolutionType(
            successListener = {
                successListener(it.mapNotNull { item -> item.name })
            },
            failureListener = failureListener
        )
    }

    suspend fun insertEvolution(
        list: List<EvolutionInfo>,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) {
        client.insertEvolution(
            list = list,
            successListener = successListener,
            failureListener = failureListener
        )
    }

}