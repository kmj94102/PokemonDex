package com.example.pokemondex.view.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemondex.network.data.PokemonListItem
import com.example.pokemondex.network.data.SearchInfo
import com.example.pokemondex.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: ListRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private lateinit var group: String

    private val _stateFlow = MutableStateFlow<Event>(Event.Init)
    val stateFlow : StateFlow<Event> = _stateFlow

    private val _pokemonList = mutableStateListOf<PokemonListItem>()
    val pokemonList: List<PokemonListItem> = _pokemonList

    private val _imageState = mutableStateOf(false)
    val imageState: State<Boolean> = _imageState

    private val _searchState = mutableStateOf("")
    val searchState : State<String> = _searchState

    init {
        savedStateHandle.get<String>("group")?.let { generation ->
            group = generation
            viewModelScope.launch {
                repository.selectPokemonList(
                    generation = generation,
                    successListener = {
                        _pokemonList.addAll(it)
                    },
                    failureListener = {
                        _pokemonList.isEmpty()
                    }
                )
                _stateFlow.emit(Event.Complete)
            }
        }
    }

    fun event(event: ListEvent) {
        when(event) {
            is ListEvent.SearchTextFieldChange -> {
                _searchState.value = event.value
            }
            is ListEvent.ImageStateChange -> {
                _imageState.value = _imageState.value.not()
            }
            is ListEvent.Search -> {
                searchPokemons()
            }
        }
    }

    private fun searchPokemons() = viewModelScope.launch {
        _pokemonList.clear()
        _stateFlow.emit(Event.Init)

        repository.selectSearchPokemonList(
            searchInfo = SearchInfo(
                generations = getGenerationList(),
                searchText = _searchState.value
            ),
            successListener = {
                _pokemonList.addAll(it)
            },
            failureListener = {
                _pokemonList.isEmpty()
            }
        )

        _stateFlow.emit(Event.Complete)
    }

    private fun getGenerationList(): List<Int> {
        return if (group == "all") {
            listOf(1,2,3,4,5,6,7,8)
        } else {
            listOf(group.toInt())
        }
    }

    sealed class Event {
        object Init: Event()
        object Complete: Event()
    }

}