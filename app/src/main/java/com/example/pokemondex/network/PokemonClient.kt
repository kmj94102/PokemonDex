package com.example.pokemondex.network

import com.example.pokemondex.network.data.*
import javax.inject.Inject

class PokemonClient @Inject constructor(
    private val service: PokemonService
){

    suspend fun insertPokemon(
        pokemon: Pokemon,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.insertPokemonInfo(pokemon))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun insertCharacteristic(
        characteristic: Characteristic,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.insertCharacteristic(characteristic))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun selectPokemonList(
        generation: String,
        successListener: (List<PokemonListItemTemp>) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.getPokemonList(generation))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun selectPokemon(
        number: String,
        successListener: (PokemonResult) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.getPokemon(number))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun selectPokemonImage(
        number: String,
        successListener: (PokemonListItemTemp) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.getPokemonImage(number))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun selectEvolutionType(
        successListener: (List<EvolutionType>) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.getEvolutionType())
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun insertEvolution(
        list : List<EvolutionInfo>,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.insertEvolution(list))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun selectSearchPokemonList(
        searchInfo: SearchInfo,
        successListener: (List<PokemonListItemTemp>) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.searchPokemonList(searchInfo))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun selectEvolutionInfo(
        number: String,
        successListener: (List<UpdateEvolutionInfoResult>) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.getEvolutionInfo(number))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun updateEvolution(
        param: EvolutionUpdateParam,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.updateEvolution(param))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun insertNewPokemonDex(
        param: InsertNewDexParam,
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.insertNewDex(param))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun selectNewDex(
        param: SelectInfo,
        successListener: (NewDexSelectResult) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.selectNewDex(param))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun selectEvolutionIno(
        param: SelectInfo,
        successListener: (List<EvolutionInfo>) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(service.selectEvolutionIno(param))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}