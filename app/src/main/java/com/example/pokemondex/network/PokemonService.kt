package com.example.pokemondex.network

import com.example.pokemondex.network.data.Characteristic
import com.example.pokemondex.network.data.Pokemon
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PokemonService {

    @GET("pokemons")
    suspend fun getPokemonList(): List<Pokemon>

    @POST("pokemon")
    suspend fun insertPokemonInfo(@Body pokemon: Pokemon): String

    @POST("char")
    suspend fun insertCharacteristic(@Body characteristic: Characteristic): String

}