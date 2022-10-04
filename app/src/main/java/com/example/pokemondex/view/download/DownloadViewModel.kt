package com.example.pokemondex.view.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemondex.network.data.SelectInfo
import com.example.pokemondex.repository.DownloadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val repository: DownloadRepository
) : ViewModel() {

    private val _eventFlow = MutableStateFlow<Event>(Event.Init)
    val eventFlow: StateFlow<Event> = _eventFlow

    fun event(event: DownloadEvent) {
        when (event) {
            is DownloadEvent.NewPokemonInsert -> {
                insertNewPokemonInfo(event)
            }
            is DownloadEvent.EvolutionInsert -> {
                insertEvolutionInfo(event)
            }
            is DownloadEvent.EvolutionTypeInsert -> {
                insertEvolutionTypeInfo()
            }
        }
    }

    private fun insertNewPokemonInfo(event: DownloadEvent.NewPokemonInsert) =
        viewModelScope.launch {
            _eventFlow.value = Event.Loading

            repository.selectNewDex(
                param = SelectInfo(
                    startNum = event.startIndex,
                    endNum = event.endIndex
                ),
                successListener = {
                    _eventFlow.value = Event.Success
                },
                failureListener = {
                    _eventFlow.value = Event.Error
                }
            )
        }

    private fun insertEvolutionInfo(event: DownloadEvent.EvolutionInsert) =
        viewModelScope.launch {
            _eventFlow.value = Event.Loading

            repository.selectEvolutionIno(
                param = SelectInfo(
                    startNum = event.startIndex,
                    endNum = event.endIndex
                ),
                successListener = {
                    _eventFlow.value = Event.Success
                },
                failureListener = {
                    _eventFlow.value = Event.Error
                }
            )
        }

    private fun insertEvolutionTypeInfo() = viewModelScope.launch {
        _eventFlow.value = Event.Loading

        repository.selectEvolutionType(
            successListener = {
                _eventFlow.value = Event.Success
            },
            failureListener = {
                _eventFlow.value = Event.Error
            }
        )
    }

    sealed class Event {
        object Init : Event()
        object Loading : Event()
        object Success : Event()
        object Error : Event()
    }

}