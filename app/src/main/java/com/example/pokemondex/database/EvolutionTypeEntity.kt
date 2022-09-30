package com.example.pokemondex.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EvolutionTypeEntity(
    @PrimaryKey(autoGenerate = true) val index: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image") val image: String
)