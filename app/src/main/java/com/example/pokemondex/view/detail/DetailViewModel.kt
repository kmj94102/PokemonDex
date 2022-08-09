package com.example.pokemondex.view.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemondex.network.data.PokemonItem
import com.example.pokemondex.repository.DetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: DetailRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _pokemonInfo = mutableStateOf(PokemonItem())
    val pokemonInfo: State<PokemonItem> = _pokemonInfo

    init {
        savedStateHandle.get<String>("number")?.let {
            viewModelScope.launch {
                repository.getPokemonInfo(
                    number = it,
                    successListener = {
                        _pokemonInfo.value = it
                    },
                    failureListener = {

                    }
                )
            }
        }
    }

}