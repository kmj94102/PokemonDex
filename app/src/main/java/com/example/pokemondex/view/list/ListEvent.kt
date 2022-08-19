package com.example.pokemondex.view.list

sealed class ListEvent {

    data class SearchTextFieldChange(
        val value: String
    ): ListEvent()

    data class TypeCondition(
        val typeIndex: Int
    ): ListEvent()

    data class GenerateCondition(
        val index: Int
    ): ListEvent()

    object ImageStateChange: ListEvent()

    object Search: ListEvent()

    object SettingCondition: ListEvent()

    object MenuOpen: ListEvent()

}