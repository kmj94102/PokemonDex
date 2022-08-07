package com.example.pokemondex.network.data

data class Pokemon(
    val number: String?,
    val name: String?,
    val status: String?,
    val classification: String?,
    val characteristic: String?,
    val attribute: String?,
    val dotImage: String?,
    val dotShinyImage: String?,
    val image: String?,
    val shinyImage: String?,
    val description: String?,
    val generation: Int?
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