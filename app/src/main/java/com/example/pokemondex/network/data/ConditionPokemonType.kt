package com.example.pokemondex.network.data

data class ConditionPokemonType(
    val name: String,
    val imageRes: Int,
    var isSelect: Boolean = true
)

fun TypeInfo.mapper(): ConditionPokemonType {
    return ConditionPokemonType(
        name = koreanName,
        imageRes = imageRes,
        isSelect = true
    )
}

data class ConditionGenerate(
    val generate: String,
    var isSelect: Boolean = false
)

fun getConditionGenerateList() =
    listOf(
        ConditionGenerate("1"),
        ConditionGenerate("2"),
        ConditionGenerate("3"),
        ConditionGenerate("4"),
        ConditionGenerate("5"),
        ConditionGenerate("6"),
        ConditionGenerate("7"),
        ConditionGenerate("8"),
        ConditionGenerate("9"),
    )