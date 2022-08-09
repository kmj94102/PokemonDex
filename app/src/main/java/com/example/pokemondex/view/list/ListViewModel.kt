package com.example.pokemondex.view.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemondex.network.data.PokemonListItem
import com.example.pokemondex.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: ListRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _pokemonList = mutableStateListOf<PokemonListItem>()
    val pokemonList: List<PokemonListItem> = _pokemonList

    private val _imageState = mutableStateOf(false)
    val imageState: State<Boolean> = _imageState

    private val _searchState = mutableStateOf("")
    val searchState : State<String> = _searchState

    init {
        savedStateHandle.get<String>("group")?.let {
            viewModelScope.launch {
                repository.selectPokemonList(
                    successListener = {
                        _pokemonList.addAll(it)
                    },
                    failureListener = {
                        _pokemonList.isEmpty()
                    }
                )
            }
        }
    }

    fun event(event: ListEvent) {
        when(event) {
            is ListEvent.SearchChange -> {
                _searchState.value = event.value
            }
            is ListEvent.ImageStateChange -> {
                _imageState.value = _imageState.value.not()
            }
        }

    }

}