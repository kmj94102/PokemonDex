package com.example.pokemondex.view.list

sealed class ListEvent {

    data class SearchTextFieldChange(
        val value: String
    ): ListEvent()

    object ImageStateChange: ListEvent()

    object Search: ListEvent()

}