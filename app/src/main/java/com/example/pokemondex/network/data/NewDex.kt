package com.example.pokemondex.network.data

data class InsertNewDexParam(
    val number: String,
    val allDexNumber: String,
    val type: String
)

data class NewDexInfo(
    var number: String = "",
    var allDexNumber: String = "",
    var type: String = "A"
) {
    fun mapper(): InsertNewDexParam? {
        if (number.isEmpty() || allDexNumber.isEmpty()) {
            return null
        }
        return InsertNewDexParam(
            number = number,
            allDexNumber = allDexNumber,
            type = type
        )
    }
}