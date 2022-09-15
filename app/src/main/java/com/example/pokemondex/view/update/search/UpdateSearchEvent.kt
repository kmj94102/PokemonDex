package com.example.pokemondex.view.update.search

sealed class UpdateSearchEvent {
    data class SearchTextFieldChange(val text: String): UpdateSearchEvent()
    object Search: UpdateSearchEvent()
}