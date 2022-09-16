package com.example.pokemondex.view.update.pokemon

sealed class NewDexEvent {
    data class DexNumber(val number: String): NewDexEvent()
    data class Complete(
        val successListener: (String) -> Unit,
        val failureListener: () -> Unit
    ): NewDexEvent()
}
