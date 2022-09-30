package com.example.pokemondex.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EvolutionEntity(
    @PrimaryKey(autoGenerate = true) val index: Int,
    @ColumnInfo(name = "numbers") val numbers: String,
    @ColumnInfo(name = "beforeNum") val beforeNum: String,
    @ColumnInfo(name = "afterNum") val afterNum: String,
    @ColumnInfo(name = "evolutionType") val evolutionType: String,
    @ColumnInfo(name = "evolutionConditions") val evolutionConditions: String
)
