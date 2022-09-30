package com.example.pokemondex.database

import javax.inject.Inject

class DatabaseClient @Inject constructor(
    private val dao: PokemonDao
) {

    suspend fun insertPokemonList(
        pokemonEntities: List<PokemonEntity>,
        successListener: (Unit) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(dao.insertPokemonList(pokemonEntities))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun insertEvolution(
        evolutionEntities: List<EvolutionEntity>,
        successListener: (Unit) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(dao.insertEvolution(evolutionEntities))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun insertEvolutionType(
        evolutionTypeEntities: List<EvolutionTypeEntity>,
        successListener: (Unit) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(dao.insertEvolutionType(evolutionTypeEntities))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

    suspend fun insertCollection(
        collectionEntities: List<CollectionEntity>,
        successListener: (Unit) -> Unit,
        failureListener: () -> Unit
    ) = try {
        successListener(dao.insertCollection(collectionEntities))
    } catch (e: Exception) {
        e.printStackTrace()
        failureListener()
    }

}