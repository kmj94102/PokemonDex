package com.example.pokemondex.network.data

import com.google.gson.annotations.SerializedName

data class ExternalPokemonInfo(
    val abilities: List<Ability>?,
    val stats: List<PokemonStatus>?,
    val types: List<PokemonType>?
)

data class Ability(
    val ability: InfoDetail?
)

data class PokemonStatus(
    @SerializedName("base_stat")
    val baseStat: Int?,
    val stat: InfoDetail?,
)

data class InfoDetail(
    val name: String?,
    val url: String?
)

data class PokemonType(
    val slot: Int?,
    val type: InfoDetail?
)

data class PokemonInfoResult(
    val abilities: List<String>,
    val status: Map<String, Int>,
    val typeList: List<String>
)

fun PokemonStatus.mapper(): Pair<String, Int>? {
    baseStat ?: return null
    stat?.name ?: return null

    return Pair(getStatusKoreanName(stat.name), baseStat)
}

fun PokemonType.mapper(): String? {
    type?.name?.let {
        return getTypeKoreaName(it)
    } ?: return null
}

fun ExternalPokemonInfo.mapper(): PokemonInfoResult {

    val ability = mutableListOf<String>()
    val status = mutableMapOf<String, Int>()
    val typeList = mutableListOf<String>()

    abilities?.mapNotNull { it.ability?.name }?.let {
        ability.addAll(it)
    }

    types?.mapNotNull { it.mapper() }?.let {
        typeList.addAll(it)
    }

    stats?.mapNotNull { it.mapper() }?.forEach {
        status[it.first] = it.second
    }

    return PokemonInfoResult(
        abilities = ability,
        status = status,
        typeList = typeList
    )
}

data class SpeciesInfo(
    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry>,
    val genera: List<Genera>,
    val names: List<PokemonName>
) {
    fun mapper(): SpeciesInfoResult? {
        val description = flavorTextEntries
            .lastOrNull { it.language.name == "ko" }
            ?.flavorText
            ?.replace("\n", " ")
            ?: return null
        val classification = genera
            .lastOrNull { it.language.name == "ko" }
            ?.genus
            ?: return null
        val name = names
            .lastOrNull { it.language.name == "ko" }
            ?.name
            ?: return null

        return SpeciesInfoResult(
            description = description,
            classification = classification,
            name = name
        )
    }
}

data class FlavorTextEntry(
    @SerializedName("flavor_text")
    val flavorText: String,
    val language: Language
)

data class Language(
    val name: String
)

data class Genera(
    val genus: String,
    val language: Language
)

data class PokemonName(
    val name: String,
    val language: Language
)

data class SpeciesInfoResult(
    val description: String,
    val classification: String,
    val name: String
)

data class AbilityInfo(
    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry>,
    val names: List<PokemonName>
) {
    fun mapper(): AbilityInfoResult? {
        val description = flavorTextEntries
            .lastOrNull { it.language.name == "ko" }
            ?.flavorText
            ?.replace("\n", " ")
            ?: return null

        val name = names
            .lastOrNull { it.language.name == "ko" }
            ?.name
            ?: return null

        return AbilityInfoResult(
            description = description,
            name = name
        )
    }
}

data class AbilityInfoResult(
    val name: String,
    val description: String
)