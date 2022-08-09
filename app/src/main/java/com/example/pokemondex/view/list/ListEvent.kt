package com.example.pokemondex.view.list

sealed class ListEvent {

    data class SearchChange(
        val value: String
    ): ListEvent()

    object ImageStateChange: ListEvent()

}