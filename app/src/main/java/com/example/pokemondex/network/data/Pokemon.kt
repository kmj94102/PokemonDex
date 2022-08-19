package com.example.pokemondex.network.data

data class PokemonResult(
    val info: Pokemon,
    val before: PokemonListItemTemp?,
    val after: PokemonListItemTemp?,
    val evolution: List<EvolutionResult>
) {
    fun itemMapper() : PokemonItem? {
        return PokemonItem(
            index = info.index ?: return null,
            number = info.number ?: return null,
            name = info.name ?: return null,
            status = info.status ?: return null,
            classification = info.classification ?: return null,
            characteristic = info.characteristic ?: return null,
            attribute = info.attribute ?: return null,
            dotImage = info.dotImage ?: return null,
            dotShinyImage = info.dotShinyImage ?: return null,
            image = info.image ?: return null,
            shinyImage = info.shinyImage ?: return null,
            description = info.description ?: return null,
            generation = info.generation ?: return null,
            before = before?.mapper(),
            after = after?.mapper(),
            evolutionList = evolution.mapNotNull { it.mapper() }
        )
    }
}


data class Pokemon(
    var index: Long?= null,
    var number: String?= null,
    var name: String?= null,
    var status: String?= null,
    var classification: String?= null,
    var characteristic: String?= null,
    var attribute: String?= null,
    var dotImage: String?= null,
    var dotShinyImage: String?= null,
    var image: String?= null,
    var shinyImage: String?= null,
    var description: String?= null,
    var generation: Int?= null
)

data class PokemonItem(
    val index: Long = 0L,
    val number: String = "",
    val name: String = "",
    val status: String = "",
    val classification: String = "",
    val characteristic: String = "",
    val attribute: String = "",
    val dotImage: String = "",
    val dotShinyImage: String = "",
    val image: String = "",
    val shinyImage: String = "",
    val description: String = "",
    val generation: Int = 0,
    val before: PokemonListItem? = null,
    val after: PokemonListItem? = null,
    val evolutionList: List<Evolution> = emptyList()
)

data class PokemonListItemTemp(
    val number: String?= null,
    val name: String?= null,
    val dotImage: String?= null,
    val dotShinyImage: String?= null,
    val attribute: String?= null
) {
    fun mapper(): PokemonListItem? {
        return PokemonListItem(
            number = number ?: return null,
            name = name ?: return null,
            dotImage = dotImage ?: return null,
            dotShinyImage = dotShinyImage ?: return null,
            attribute = attribute ?: return null
        )
    }
}

data class PokemonListItem(
    val number: String,
    val name: String,
    val dotImage: String,
    val dotShinyImage: String,
    val attribute: String
)

data class PokemonStatusInfo(
    val name: String,
    var value: Int
)