package com.example.pokemondex.view.new_dex

sealed class NewDexEvent {

    data class Update(
        val group: String,
        val number: String,
        val isChecked: Boolean
    ) : NewDexEvent()

    data class Search(
        val text: String
    ) : NewDexEvent()

    data class SelectInfo(
        val type: String
    ): NewDexEvent()

}