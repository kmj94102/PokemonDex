package com.example.pokemondex.network.data

import com.example.pokemondex.database.EvolutionEntity
import com.example.pokemondex.database.EvolutionTypeEntity
import retrofit2.http.Body

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
    var index: Int = 0,
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
            index = index,
            numbers = numbers,
            beforeNum = beforeNum,
            afterNum = afterNum,
            evolutionType = evolutionType,
            evolutionConditions = evolutionConditions,
        )
    }
}

data class EvolutionInfo(
    val index: Int = 0,
    val numbers: String,
    val beforeNum: String,
    val afterNum: String,
    val evolutionType: String,
    val evolutionConditions: String
) {
    fun mapper(): EvolutionEntity {
        return EvolutionEntity(
            index = 0,
            numbers = numbers,
            beforeNum = beforeNum,
            afterNum = afterNum,
            evolutionType = evolutionType,
            evolutionConditions = evolutionConditions
        )
    }
}

data class UpdateEvolutionInfoResult(
    val index: Int?,
    val numbers: String?,
    val beforeNum: String?,
    val afterNum: String?,
    val evolutionType: String?,
    val evolutionConditions: String?,
    val beforeImage: String?,
    val afterImage: String?
) {
    fun mapper(): EvolutionTemp? {
        return EvolutionTemp(
            index = index ?: return null,
            numbers = numbers ?: return null,
            beforeNum = beforeNum ?: return null,
            afterNum = afterNum ?: return null,
            evolutionType = evolutionType ?: return null,
            evolutionConditions = evolutionConditions ?: return null,
            beforeImage = beforeImage ?: return null,
            afterImage = afterImage ?: return null
        )
    }
}

data class EvolutionUpdateParam(
    val items: List<EvolutionInfo>,
    val removeItems: List<Int>
)

data class EvolutionType(
    val name: String?= null,
    val image: String?= null
) {
    fun mapper(): EvolutionTypeEntity? {
        return EvolutionTypeEntity(
            index = 0,
            name = name ?: return null,
            image = image ?: return null
        )
    }
}