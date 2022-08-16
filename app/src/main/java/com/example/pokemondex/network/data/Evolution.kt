package com.example.pokemondex.network.data

data class EvolutionResult(
    val numbers: String?= null,
    val beforeNum: String?= null,
    val afterNum: String?= null,
    val evolutionType: String?= null,
    val evolutionConditions: String?= null
) {
    fun mapper(): EvolutionInfo? {
        return EvolutionInfo(
            numbers = numbers ?: return null,
            beforeNum = beforeNum ?: return null,
            afterNum = afterNum ?: return null,
            evolutionType = evolutionType ?: return null,
            evolutionConditions = evolutionConditions ?: return null,
        )
    }
}

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