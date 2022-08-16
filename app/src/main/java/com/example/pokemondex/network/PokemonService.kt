package com.example.pokemondex.network

import com.example.pokemondex.network.data.*
import retrofit2.http.*

interface PokemonService {

    @GET("pokemons/{generation}")
    suspend fun getPokemonList(@Path("generation") generation: String): List<PokemonListItemTemp>

    @GET("pokemon/number/{number}")
    suspend fun getPokemon(@Path("number") number: String): PokemonResult

    @GET("/pokemon/number/image/{number}")
    suspend fun getPokemonImage(@Path("number") number: String): PokemonListItemTemp

    @POST("pokemon")
    suspend fun insertPokemonInfo(@Body pokemon: Pokemon): String

    @POST("char")
    suspend fun insertCharacteristic(@Body characteristic: Characteristic): String

    @GET("/evolution/type")
    suspend fun getEvolutionType(): List<Name>

    @POST("/evolutions")
    suspend fun insertEvolution(@Body evolutionInfoList: List<EvolutionInfo>): String

    @POST("/pokemons/search")
    suspend fun searchPokemonList(@Body info: SearchInfo): List<PokemonListItemTemp>

}