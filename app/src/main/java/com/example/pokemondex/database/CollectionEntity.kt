package com.example.pokemondex.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CollectionEntity(
    @PrimaryKey(autoGenerate = true) val index: Long,
    @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "normal") val normal: Boolean,
    @ColumnInfo(name = "shiny") val shiny: Boolean,
    @ColumnInfo(name = "importance") val importance: Boolean
)

data class CollectionPokemon(
    val index: Long,
    val number: String,
    val name: String,
    val dotImage: String,
    val normal: Boolean,
    val shiny: Boolean,
    val importance: Boolean
)