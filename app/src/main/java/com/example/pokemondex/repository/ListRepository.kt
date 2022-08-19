package com.example.pokemondex.repository

import com.example.pokemondex.network.PokemonClient
import com.example.pokemondex.network.data.PokemonListItem
import com.example.pokemondex.network.data.SearchInfo
import javax.inject.Inject

class ListRepository @Inject constructor(
    private val client: PokemonClient
) {

    suspend fun selectPokemonList(
        generation: String,
        successListener: (List<PokemonListItem>) -> Unit,
        failureListener: () -> Unit
    ) {
        client.selectPokemonList(
            generation = generation,
            successListener = {
                it.mapNotNull { item -> item.mapper() }.let(successListener)
            },
            failureListener = failureListener
        )
    }

    suspend fun selectSearchPokemonList(
        searchInfo: SearchInfo,
        typeList: List<String>,
        successListener: (List<PokemonListItem>) -> Unit,
        failureListener: () -> Unit
    ) {
        client.selectSearchPokemonList(
            searchInfo = searchInfo,
            successListener = {
                it.mapNotNull { item -> item.mapper() }.filter { item ->
                    var result = false
                    item.attribute.split(",").forEach { type ->
                        if (result) return@forEach
                        result = typeList.contains(type)
                    }
                    result
                }.let(successListener)
            },
            failureListener = failureListener
        )
    }

}