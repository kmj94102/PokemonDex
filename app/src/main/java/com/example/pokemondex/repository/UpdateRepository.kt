package com.example.pokemondex.repository

import com.example.pokemondex.network.PokemonClient
import com.example.pokemondex.network.data.*
import javax.inject.Inject

class UpdateRepository @Inject constructor(
    private val client: PokemonClient
) {

    suspend fun getEvolutionInfo(
        number: String,
        successListener: (List<EvolutionTemp>) -> Unit,
        failureListener: () -> Unit
    ){
        client.selectEvolutionInfo(
            number = number,
            successListener = { result ->
                successListener(result.mapNotNull { it.mapper() })
            },
            failureListener = failureListener
        )
    }

    suspend fun updateEvolution(
        param: EvolutionUpdateParam,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) {
        client.updateEvolution(
            param = param,
            successListener = successListener,
            failureListener = failureListener
        )
    }

}