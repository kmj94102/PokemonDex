package com.example.pokemondex.view.new_dex.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemondex.database.CollectionPokemonDetail
import com.example.pokemondex.database.PokemonButtonInfo
import com.example.pokemondex.network.data.Evolution
import com.example.pokemondex.repository.NewDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewDetailViewModel @Inject constructor(
    private val repository: NewDetailRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _beforeButtonInfo = mutableStateOf<PokemonButtonInfo?>(null)
    val beforeButtonInfo: State<PokemonButtonInfo?> = _beforeButtonInfo

    private val _afterButtonInfo = mutableStateOf<PokemonButtonInfo?>(null)
    val afterButtonInfo: State<PokemonButtonInfo?> = _afterButtonInfo

    private val _evolutionInfo = mutableStateListOf<Evolution>()
    val evolutionInfo: List<Evolution> = _evolutionInfo

    private val _pokemonInfo = mutableStateOf(CollectionPokemonDetail())
    val pokemonInfo: State<CollectionPokemonDetail> = _pokemonInfo

    init {
        savedStateHandle.get<Long>("number")?.let {
            viewModelScope.launch {
                repository.selectButtonsInfo(
                    number = it,
                    result = { before, after ->
                        _beforeButtonInfo.value = before
                        _afterButtonInfo.value = after
                    }
                )
            }


        }

        savedStateHandle.get<String>("allDexNumber")?.let { number ->
            viewModelScope.launch {
                _evolutionInfo.clear()
                _evolutionInfo.addAll(repository.selectEvolutionInfo(allDexNumber = number))
            }

            repository.selectPokemonInfo(number)
                .onEach { info ->
                    _pokemonInfo.value = info
                }
                .catch {
                    _pokemonInfo.value = CollectionPokemonDetail()
                }
                .launchIn(viewModelScope)
        }
    }

}