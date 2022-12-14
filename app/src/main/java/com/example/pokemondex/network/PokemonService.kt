package com.example.pokemondex.network

import com.example.pokemondex.network.data.*
import retrofit2.http.*

interface PokemonService {

    @GET("/pokemons/{generation}")
    suspend fun getPokemonList(@Path("generation") generation: String): List<PokemonListItemTemp>

    @GET("/pokemon/number/{number}")
    suspend fun getPokemon(@Path("number") number: String): PokemonResult

    @GET("/pokemon/number/image/{number}")
    suspend fun getPokemonImage(@Path("number") number: String): PokemonListItemTemp

    @POST("/pokemon")
    suspend fun insertPokemonInfo(@Body pokemon: Pokemon): String

    @POST("/char")
    suspend fun insertCharacteristic(@Body characteristic: Characteristic): String

    @GET("/evolution/type")
    suspend fun getEvolutionType(): List<EvolutionType>

    @POST("/evolutions")
    suspend fun insertEvolution(@Body evolutionInfoList: List<EvolutionInfo>): String

    @POST("/pokemons/search")
    suspend fun searchPokemonList(@Body info: SearchInfo): List<PokemonListItemTemp>

    @GET("/pokemon/evolution/{number}")
    suspend fun getEvolutionInfo(@Path("number") number: String): List<UpdateEvolutionInfoResult>

    @POST("/pokemon/evolution/update")
    suspend fun updateEvolution(@Body param: EvolutionUpdateParam): String

    @POST("/newDex")
    suspend fun insertNewDex(@Body param: InsertNewDexParam): String

    @POST("/newDex/select")
    suspend fun selectNewDex(@Body param: SelectInfo): NewDexSelectResult

    @POST("/evolutions/select")
    suspend fun selectEvolutionIno(@Body param: SelectInfo): List<EvolutionInfo>

}