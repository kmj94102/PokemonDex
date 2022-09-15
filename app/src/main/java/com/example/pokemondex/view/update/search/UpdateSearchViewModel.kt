package com.example.pokemondex.view.update.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemondex.network.data.PokemonListItem
import com.example.pokemondex.network.data.SearchInfo
import com.example.pokemondex.network.data.TypeInfo
import com.example.pokemondex.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateSearchViewModel @Inject constructor(
    private val repository: ListRepository
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<Event>(Event.Init)
    val eventStateFlow : StateFlow<Event> = _stateFlow

    private val _searchState = mutableStateOf("")
    val searchState: State<String> = _searchState

    private val _pokemonList = mutableStateListOf<PokemonListItem>()
    val pokemonList: List<PokemonListItem> = _pokemonList

    fun event(event: UpdateSearchEvent) {
        when(event) {
            is UpdateSearchEvent.SearchTextFieldChange -> {
                _searchState.value = event.text
            }
            is UpdateSearchEvent.Search -> {
                selectPokemonList()
            }
        }
    }

    private fun selectPokemonList() = viewModelScope.launch {
        _pokemonList.clear()
        _stateFlow.emit(Event.Loading)

        repository.selectSearchPokemonList(
            searchInfo = SearchInfo(
                generations = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
                searchText = _searchState.value
            ),
            typeList = TypeInfo.values().map { it.koreanName }.dropLast(1),
            successListener = {
                _pokemonList.addAll(it)
            },
            failureListener = { }
        )

        _stateFlow.emit(Event.Complete)
    }

    sealed class Event {
        object Init: Event()
        object Loading: Event()
        object Complete: Event()
    }

}