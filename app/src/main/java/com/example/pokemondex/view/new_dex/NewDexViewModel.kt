package com.example.pokemondex.view.new_dex

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemondex.database.CollectionPokemon
import com.example.pokemondex.repository.NewDexRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewDexViewModel @Inject constructor(
    private val repository: NewDexRepository
) : ViewModel() {

    private val _pokemonList = mutableStateOf<List<CollectionPokemon>>(emptyList())
    val pokemonList: State<List<CollectionPokemon>> = _pokemonList

    private val _normalCollect = mutableStateOf(0)
    val normalCollect: State<Int> = _normalCollect

    private val _shinyCollect = mutableStateOf(0)
    val shinyCollect: State<Int> = _shinyCollect

    private var searchText = ""
    private var searchType = All

    init {
        selectPokemonList()
        selectNormalCollect()
        selectShinyCollect()
    }

    fun event(event: NewDexEvent) {
        when (event) {
            is NewDexEvent.Update -> {
                when (event.group) {
                    Normal -> {
                        updateNormal(event)
                    }
                    Shiny -> {
                        updateShiny(event)
                    }
                    Importance -> {
                        updateImportance(event)
                    }
                }
            }
            is NewDexEvent.Search -> {
                searchText = event.text
                selectPokemonList()
            }
            is NewDexEvent.SelectInfo -> {
                searchType = event.type
                selectPokemonList()
            }
        }
    }

    private fun selectPokemonList() {
        repository.selectPokemonList(name = searchText)
            .map {
                when (searchType) {
                    NonCollection -> it.filter { item -> !item.normal || !item.shiny }
                    Importance -> it.filter { item -> item.importance }
                    else -> it
                }
            }
            .onEach { _pokemonList.value = it }
            .catch { _pokemonList.value = emptyList() }
            .launchIn(viewModelScope)
    }

    private fun selectNormalCollect() {
        repository.selectNormalCollect()
            .onEach { _normalCollect.value = it }
            .catch { _normalCollect.value = 0 }
            .launchIn(viewModelScope)
    }

    private fun selectShinyCollect() {
        repository.selectShinyCollect()
            .onEach { _shinyCollect.value = it }
            .catch { _shinyCollect.value = 0 }
            .launchIn(viewModelScope)
    }

    private fun updateNormal(event: NewDexEvent.Update) = viewModelScope.launch {
        repository.updateNormal(
            number = event.number,
            normal = event.isChecked,
            successListener = {},
            failureListener = {}
        )
    }

    private fun updateShiny(event: NewDexEvent.Update) = viewModelScope.launch {
        repository.updateShiny(
            number = event.number,
            shiny = event.isChecked,
            successListener = {},
            failureListener = {}
        )
    }

    private fun updateImportance(event: NewDexEvent.Update) = viewModelScope.launch {
        repository.updateImportance(
            number = event.number,
            importance = event.isChecked,
            successListener = {},
            failureListener = {}
        )
    }

    companion object {
        const val Normal = "normal"
        const val Shiny = "shiny"
        const val Importance = "importance"
        const val All = "all"
        const val NonCollection = "non_collection"
    }

}