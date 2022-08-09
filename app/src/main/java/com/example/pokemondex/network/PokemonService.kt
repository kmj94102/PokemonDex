package com.example.pokemondex.network

import com.example.pokemondex.network.data.Characteristic
import com.example.pokemondex.network.data.Pokemon
import com.example.pokemondex.network.data.PokemonListItemTemp
import retrofit2.http.*

interface PokemonService {

    @GET("pokemons")
    suspend fun getPokemonList(): List<PokemonListItemTemp>

    @GET("pokemon/number/{number}")
    suspend fun getPokemon(@Path("number") number: String): Pokemon

    @POST("pokemon")
    suspend fun insertPokemonInfo(@Body pokemon: Pokemon): String

    @POST("char")
    suspend fun insertCharacteristic(@Body characteristic: Characteristic): String

}