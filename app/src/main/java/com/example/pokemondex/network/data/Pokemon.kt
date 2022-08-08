package com.example.pokemondex.network.data

data class Pokemon(
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
) {
    fun itemMapper() : PokemonItem? {
        return PokemonItem(
            number = number ?: return null,
            name = name ?: return null,
            status = status ?: return null,
            classification = classification ?: return null,
            characteristic = characteristic ?: return null,
            attribute = attribute ?: return null,
            dotImage = dotImage ?: return null,
            dotShinyImage = dotShinyImage ?: return null,
            image = image ?: return null,
            shinyImage = shinyImage ?: return null,
            description = description ?: return null,
            generation = generation ?: return null
        )
    }
}

data class PokemonItem(
    val number: String,
    val name: String,
    val status: String,
    val classification: String,
    val characteristic: String,
    val attribute: String,
    val dotImage: String,
    val dotShinyImage: String,
    val image: String,
    val shinyImage: String,
    val description: String,
    val generation: Int
)

data class PokemonStatusInfo(
    val name: String,
    var value: Int
)