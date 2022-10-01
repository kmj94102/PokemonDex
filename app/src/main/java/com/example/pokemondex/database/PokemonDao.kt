package com.example.pokemondex.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(pokemonEntities: List<PokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvolution(evolutionEntities: List<EvolutionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvolutionType(evolutionTypeEntities: List<EvolutionTypeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collectionEntities: List<CollectionEntity>)

    @Query("SELECT po.`index`, po.number, po.name, po.dotImage, co.normal, co.shiny, co.importance " +
            "FROM PokemonEntity as po, CollectionEntity as co " +
            "WHERE po.generation = 'arceus' AND po.number = co.number AND po.name like :name")
    fun selectPokemonList(name: String): Flow<List<CollectionPokemon>>

    @Query("SELECT COUNT(*) FROM CollectionEntity WHERE normal = 1")
    fun selectNormalCollect(): Flow<Int>

    @Query("SELECT COUNT(*) FROM CollectionEntity WHERE shiny = 1")
    fun selectShinyCollect(): Flow<Int>

    @Query("UPDATE CollectionEntity SET `normal` = :normal WHERE number = :number")
    suspend fun updateNormal(number: String, normal: Boolean)

    @Query("UPDATE CollectionEntity SET `shiny` = :shiny WHERE number = :number")
    suspend fun updateShiny(number: String, shiny: Boolean)

    @Query("UPDATE CollectionEntity SET `importance` = :importance WHERE number = :number")
    suspend fun updateImportance(number: String, importance: Boolean)

}