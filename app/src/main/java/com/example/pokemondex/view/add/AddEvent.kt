package com.example.pokemondex.view.add

sealed class AddEvent{
    data class InputTextChange(
        val code: String,
        val value: String
    ): AddEvent()
    data class InputCharacteristic(
        val index: Int,
        val code: String,
        val value: String
    ): AddEvent()
    data class InputType(
        val index: Int,
        val value: String
    ): AddEvent()
    object AddCharacteristic: AddEvent()
    object RemoveCharacteristic: AddEvent()
    object DefaultImageSetting: AddEvent()
    data class Register(
        val successListener: (String) -> Unit,
        val failureListener: () -> Unit
    ): AddEvent()
    data class SearchPokemonInfo(
        val index: String,
        val failureListener: (String) -> Unit
    ): AddEvent()
}
