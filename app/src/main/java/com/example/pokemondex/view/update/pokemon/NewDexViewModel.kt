package com.example.pokemondex.view.update.pokemon

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemondex.network.data.NewDexInfo
import com.example.pokemondex.repository.NewDexRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewDexViewModel @Inject constructor(
    private val repository: NewDexRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _info = mutableStateOf(NewDexInfo())
    val info: State<NewDexInfo> = _info

    init {
        savedStateHandle.get<String>("index")?.let {
            _info.value.allDexNumber = it
        }
    }

    fun event(event: NewDexEvent) {
        when(event) {
            is NewDexEvent.DexNumber -> {
                _info.value = _info.value.copy(number = event.number)
            }
            is NewDexEvent.Complete -> {
                insertNewPokemonDex(event)
            }
        }
    }

    private fun insertNewPokemonDex(event: NewDexEvent.Complete) = viewModelScope.launch {
        repository.insertNewPokemonDex(
            param = _info.value.copy(number = _info.value.number.padStart(3, '0')),
            successListener = {
                event.successListener(it)
            },
            failureListener = {
                event.failureListener()
            }
        )
    }

}