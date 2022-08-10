package com.example.pokemondex.network

import com.example.pokemondex.network.data.ExternalPokemonInfo
import com.example.pokemondex.network.data.SpeciesInfo
import retrofit2.http.GET
import retrofit2.http.Path

interface ExternalService {
    @GET("v2/pokemon/{index}")
    suspend fun getPokemonDetail(
        @Path("index") index: Int = 1
    ): ExternalPokemonInfo

    @GET("v2/pokemon-species/{index}")
    suspend fun getPokemonSpecies(
        @Path("index") index: Int = 1
    ): SpeciesInfo
}