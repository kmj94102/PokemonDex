package com.example.pokemondex.view.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemondex.network.data.PokemonItem
import com.example.pokemondex.network.data.TypeInfo
import com.example.pokemondex.network.data.getWeaknessInfo
import com.example.pokemondex.repository.DetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: DetailRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _eventFlow = MutableStateFlow<Event>(Event.Init)
    val eventFlow: StateFlow<Event> = _eventFlow

    private val _pokemonInfo = mutableStateOf(PokemonItem())
    val pokemonInfo: State<PokemonItem> = _pokemonInfo

    private val _typeCompatibility = mutableListOf<Pair<Float, String>>()
    val typeCompatibility: List<Pair<Float, String>> = _typeCompatibility

    private val _isShiny = mutableStateOf(false)
    val isShiny: State<Boolean> = _isShiny

    init {
        savedStateHandle.get<String>("number")?.let {
            viewModelScope.launch {
                repository.getPokemonInfo(
                    number = it,
                    successListener = {
                        _pokemonInfo.value = it
                        getTypeCompatibility(it.attribute)
                        _eventFlow.value = Event.Success
                    },
                    failureListener = {
                        _eventFlow.value = Event.Failure
                    }
                )
            }
        }
        savedStateHandle.get<Boolean>("isShiny")?.let {
            _isShiny.value = it
        }
    }

    private fun getTypeCompatibility(attribute: String) {
        val tempList = attribute.split(",").map { getWeaknessInfo(it) }

        if (tempList.isEmpty()) return

        val list = if (tempList.size == 2) {
            tempList[0].zip(tempList[1]).map { it.first * it.second }
        } else {
            tempList[0]
        }

        _typeCompatibility.clear()
        _typeCompatibility.addAll(
            list.zip(TypeInfo.values()
                .map { it.koreanName })
                .sortedByDescending { it.first }
        )
    }

    fun changeShinyState() {
        _isShiny.value = _isShiny.value.not()
    }

    sealed class Event {
        object Init: Event()
        object Success: Event()
        object Failure: Event()
    }

}