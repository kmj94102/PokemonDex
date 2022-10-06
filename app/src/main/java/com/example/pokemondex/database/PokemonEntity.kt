package com.example.pokemondex.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true) val index: Long = 0,
    @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "allDexNumber") val allDexNumber: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "status") val status: String,
    @ColumnInfo(name = "classification") val classification: String,
    @ColumnInfo(name = "characteristic") val characteristic: String,
    @ColumnInfo(name = "attribute") val attribute: String,
    @ColumnInfo(name = "dotImage") val dotImage: String,
    @ColumnInfo(name = "dotShinyImage") val dotShinyImage: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "shinyImage") val shinyImage: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "generation") val generation: String
)