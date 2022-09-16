package com.example.pokemondex.repository

import com.example.pokemondex.network.PokemonClient
import com.example.pokemondex.network.data.InsertNewDexParam
import com.example.pokemondex.network.data.NewDexInfo
import javax.inject.Inject

class NewDexRepository @Inject constructor(
    private val client: PokemonClient
) {

    suspend fun insertNewPokemonDex(
        param: NewDexInfo,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) {
        param.mapper()?.let {
            client.insertNewPokemonDex(
                param = it,
                successListener = successListener,
                failureListener = failureListener
            )
        } ?: failureListener()
    }

}