package com.example.pokemondex.view.update.evolution

sealed class UpdateEvolutionEvent {
    data class ImageSelect(
        val index: Int,
        val failureListener: (String) -> Unit
    ) : UpdateEvolutionEvent()

    data class InputTextChange(
        val index: Int,
        val code: String,
        val value: String
    ): UpdateEvolutionEvent()

    data class UpdateEvolution(
        val resultListener: (String) -> Unit
    ): UpdateEvolutionEvent()

    object ListAdd: UpdateEvolutionEvent()
    object ListRemove: UpdateEvolutionEvent()
}
