package com.example.pokemondex.network.data

data class EvolutionResult(
    val beforeDot: String?= null,
    val beforeShinyDot: String?= null,
    val afterDot: String?= null,
    val afterShinyDot: String?= null,
    val evolutionImage: String?= null,
    val evolutionConditions: String?= null
) {
    fun mapper(): Evolution? {
        return Evolution(
            beforeDot = beforeDot ?: return null,
            beforeShinyDot = beforeShinyDot ?: return null,
            afterDot = afterDot ?: return null,
            afterShinyDot = afterShinyDot ?: return null,
            evolutionImage = evolutionImage ?: return null,
            evolutionConditions = evolutionConditions ?: return null,
        )
    }
}

data class Evolution(
    val beforeDot: String,
    val beforeShinyDot: String,
    val afterDot: String,
    val afterShinyDot: String,
    val evolutionImage: String,
    val evolutionConditions: String
)

data class EvolutionTemp(
    var numbers: String = "",
    var beforeNum: String = "",
    var afterNum: String = "",
    var evolutionType: String = "이상한사탕",
    var evolutionConditions: String = "Lv.",
    var beforeImage: String = "",
    var afterImage: String = ""
) {
    fun mapper(): EvolutionInfo? {

        if (numbers.isEmpty() || beforeNum.isEmpty() || afterNum.isEmpty() ||
                evolutionType.isEmpty() || evolutionConditions.isEmpty()) {
            return null
        }

        return EvolutionInfo(
            numbers = numbers,
            beforeNum = beforeNum,
            afterNum = afterNum,
            evolutionType = evolutionType,
            evolutionConditions = evolutionConditions,
        )
    }
}

data class EvolutionInfo(
    val numbers: String,
    val beforeNum: String,
    val afterNum: String,
    val evolutionType: String,
    val evolutionConditions: String
)

data class Name(
    val name: String?= null
)