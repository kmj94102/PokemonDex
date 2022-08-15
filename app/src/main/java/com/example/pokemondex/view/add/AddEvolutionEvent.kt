package com.example.pokemondex.view.add

sealed class AddEvolutionEvent {

    data class ImageSelect(
        val index: Int,
        val failureListener: (String) -> Unit
    ) : AddEvolutionEvent()

    data class InputTextChange(
        val index: Int,
        val code: String,
        val value: String
    ): AddEvolutionEvent()

    data class InsertEvolution(
        val resultListener: (String) -> Unit
    ): AddEvolutionEvent()

    object ListAdd: AddEvolutionEvent()
    object ListRemove: AddEvolutionEvent()

}