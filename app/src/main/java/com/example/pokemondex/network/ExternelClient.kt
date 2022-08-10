package com.example.pokemondex.network

import com.example.pokemondex.network.data.*
import javax.inject.Inject

class ExternalClient @Inject constructor(
    private val service: ExternalService
) {

    suspend fun getPokemonInfo(
        index: Int,
        successListener: (ExternalPokemonInfo) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.getPokemonDetail(index))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun getPokemonSpeciesInfo(
        index: Int,
        successListener: (SpeciesInfo) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.getPokemonSpecies(index))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}
