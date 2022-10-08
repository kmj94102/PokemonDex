package com.example.pokemondex.view.new_dex.detail

sealed class NewDetailEvent {
    object UpdateNormalState: NewDetailEvent()
    object UpdateShinyState: NewDetailEvent()
    object UpdateImportanceState: NewDetailEvent()
}
