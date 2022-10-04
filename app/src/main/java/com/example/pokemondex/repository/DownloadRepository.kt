package com.example.pokemondex.repository

import android.util.Log
import com.example.pokemondex.database.*
import com.example.pokemondex.network.PokemonClient
import com.example.pokemondex.network.data.SelectInfo
import javax.inject.Inject

class DownloadRepository @Inject constructor(
    private val client: PokemonClient,
    private val databaseClient: DatabaseClient
) {

    suspend fun selectNewDex(
        param: SelectInfo,
        successListener: (Unit) -> Unit,
        failureListener: () -> Unit
    ) {
        var list: List<PokemonEntity> = listOf()
        client.selectNewDex(
            param = param,
            successListener = {
                list = it.info.mapNotNull { info -> info.mapper() }
                Log.e("selectNewDex", "lastIndex : ${it.lastIndex}")
//                successListener(it.lastIndex, it.info.mapNotNull { info -> info.mapper() })
            },
            failureListener = failureListener
        )

        insertPokemonList(
            list = list,
            successListener = successListener,
            failureListener = failureListener
        )

        insertCollectionList(
            list = list,
            successListener = successListener,
            failureListener = failureListener
        )

    }

    private suspend fun insertPokemonList(
        list: List<PokemonEntity>,
        successListener: (Unit) -> Unit,
        failureListener: () -> Unit
    ) {
        databaseClient.insertPokemonList(
            pokemonEntities = list,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    private suspend fun insertCollectionList(
        list: List<PokemonEntity>,
        successListener: (Unit) -> Unit,
        failureListener: () -> Unit
    ) {
        val collectionList = list.map {
            CollectionEntity(
                index = 0,
                number = it.number,
                normal = false,
                shiny = false,
                importance = false
            )
        }

        databaseClient.insertCollection(
            collectionEntities = collectionList,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    suspend fun selectEvolutionIno(
        param: SelectInfo,
        successListener: (Unit) -> Unit,
        failureListener: () -> Unit
    ) {
        var list: List<EvolutionEntity> = listOf()
        client.selectEvolutionIno(
            param = param,
            successListener = {
                list = it.map { info -> info.mapper() }
            },
            failureListener = failureListener
        )
        insertEvolutionList(
            param = list,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    private suspend fun insertEvolutionList(
        param: List<EvolutionEntity>,
        successListener: (Unit) -> Unit,
        failureListener: () -> Unit
    ) {
        databaseClient.insertEvolution(
            evolutionEntities = param,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    suspend fun selectEvolutionType(
        successListener: (Unit) -> Unit,
        failureListener: () -> Unit
    ) {
        var list: List<EvolutionTypeEntity> = listOf()
        client.selectEvolutionType(
            successListener = {
                list = it.mapNotNull { type -> type.mapper() }
            },
            failureListener = failureListener
        )

        insertEvolutionType(
            list = list,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    private suspend fun insertEvolutionType(
        list: List<EvolutionTypeEntity>,
        successListener: (Unit) -> Unit,
        failureListener: () -> Unit
    ) {
        databaseClient.insertEvolutionType(
            evolutionTypeEntities = list,
            successListener = successListener,
            failureListener = failureListener
        )
    }

}