package com.example.pokemondex.repository

import com.example.pokemondex.database.DatabaseClient
import com.example.pokemondex.network.PokemonClient
import com.example.pokemondex.network.data.NewDexInfo
import javax.inject.Inject

class NewDexRepository @Inject constructor(
    private val client: PokemonClient,
    private val databaseClient: DatabaseClient
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

    fun selectPokemonList(name: String) = databaseClient.selectPokemonList("%$name%")

    fun selectNormalCollect() = databaseClient.selectNormalCollect()

    fun selectShinyCollect() = databaseClient.selectShinyCollect()

    suspend fun updateNormal(
        number: String,
        normal: Boolean,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        databaseClient.updateNormal(
            number = number,
            normal = normal,
            successListener = {
                successListener()
            },
            failureListener = failureListener
        )
    }

    suspend fun updateShiny(
        number: String,
        shiny: Boolean,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        databaseClient.updateShiny(
            number = number,
            shiny = shiny,
            successListener = {
                successListener()
            },
            failureListener = failureListener
        )
    }

    suspend fun updateImportance(
        number: String,
        importance: Boolean,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        databaseClient.updateImportance(
            number = number,
            importance = importance,
            successListener = {
                successListener()
            },
            failureListener = failureListener
        )
    }

}