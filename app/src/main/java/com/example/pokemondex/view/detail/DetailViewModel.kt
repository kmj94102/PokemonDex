package com.example.pokemondex.view.detail

import android.util.Log
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: DetailRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _pokemonInfo = mutableStateOf(PokemonItem())
    val pokemonInfo: State<PokemonItem> = _pokemonInfo

    private val _typeCompatibility = mutableListOf<Pair<Float, String>>()
    val typeCompatibility: List<Pair<Float, String>> = _typeCompatibility
    init {
        savedStateHandle.get<String>("number")?.let {
            viewModelScope.launch {
                repository.getPokemonInfo(
                    number = it,
                    successListener = {
                        _pokemonInfo.value = it
                        getTypeCompatibility(it.attribute)
                    },
                    failureListener = {

                    }
                )
            }
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
}