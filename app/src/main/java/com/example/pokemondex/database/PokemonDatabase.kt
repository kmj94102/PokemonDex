package com.example.pokemondex.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        PokemonEntity::class, EvolutionEntity::class,
        EvolutionTypeEntity::class, CollectionEntity::class
    ],
    version = 1
)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao

    companion object {
        const val Database_Name = "pokemon_dex.db"
    }

}