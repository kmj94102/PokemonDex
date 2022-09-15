package com.example.pokemondex.view.update.evolution

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemondex.network.data.EvolutionTemp
import com.example.pokemondex.network.data.EvolutionUpdateParam
import com.example.pokemondex.repository.AddEvolutionRepository
import com.example.pokemondex.repository.UpdateRepository
import com.example.pokemondex.view.add.AddEvolutionViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateEvolutionViewModel @Inject constructor(
    private val repository: UpdateRepository,
    private val evolutionRepository: AddEvolutionRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _status = MutableStateFlow<Event>(Event.Init)
    val state: StateFlow<Event> = _status

    private val _evolutionState = mutableStateListOf(EvolutionTemp())
    val evolutionState: List<EvolutionTemp> = _evolutionState

    private val _evolutionTypes = mutableStateListOf<String>()
    val evolutionTypes: List<String> = _evolutionTypes

    private val numberList: MutableList<String> = mutableListOf()
    private val removeIndexList: MutableList<Int> = mutableListOf()

    init {
        savedStateHandle.get<String>("index")?.let {
            getEvolutionInfo(it)
        }
        selectEvolutionType()
    }

    fun event(event: UpdateEvolutionEvent) {
        when(event) {
            is UpdateEvolutionEvent.ImageSelect -> {
                getPokemonImage(event)
            }
            is UpdateEvolutionEvent.InputTextChange -> {
                setTextFiledChanged(event)
            }
            is UpdateEvolutionEvent.ListAdd -> {
                _evolutionState.add(EvolutionTemp())
            }
            is UpdateEvolutionEvent.ListRemove -> {
                _evolutionState.lastOrNull()?.index?.let {
                    if (it != 0) {
                        removeIndexList.add(it)
                    }
                }
                _evolutionState.removeLastOrNull()
            }
            is UpdateEvolutionEvent.UpdateEvolution -> {
                updateEvolution(event.resultListener)
            }
        }
    }

    private fun getEvolutionInfo(index: String) = viewModelScope.launch {
        _evolutionState.clear()
        _status.value = Event.Loading

        repository.getEvolutionInfo(
            number = index,
            successListener = {
                _evolutionState.addAll(it)
            },
            failureListener = { }
        )

        _status.value = Event.Complete
    }

    private fun selectEvolutionType() = viewModelScope.launch {
        evolutionRepository.getEvolutionType(
            successListener = {
                _evolutionTypes.addAll(it)
                _evolutionTypes.distinct()
            },
            failureListener = { }
        )
    }

    private fun getPokemonImage(event: UpdateEvolutionEvent.ImageSelect) = viewModelScope.launch {
        val beforeNum = _evolutionState[event.index].beforeNum
        val afterNum = _evolutionState[event.index].afterNum

        if (beforeNum.isEmpty() || afterNum.isEmpty()) {
            event.failureListener("진화 전, 후의 번호를 먼저 입력해주세요.")
            return@launch
        }

        evolutionRepository.getPokemonImage(
            number = beforeNum,
            successListener = {
                _evolutionState[event.index] = _evolutionState[event.index].copy(
                    beforeImage = it.dotImage
                )
            },
            failureListener = {
                event.failureListener("진화 전 이미지 조회를 실패하였습니다.")
            }
        )

        evolutionRepository.getPokemonImage(
            number = afterNum,
            successListener = {
                _evolutionState[event.index] = _evolutionState[event.index].copy(
                    afterImage = it.dotImage
                )
            },
            failureListener = {
                event.failureListener("진화 후 이미지 조회를 실패하였습니다.")
            }
        )

    }

    private fun setTextFiledChanged(event: UpdateEvolutionEvent.InputTextChange) {
        when (event.code) {
            AddEvolutionViewModel.Before -> {
                _evolutionState[event.index] = _evolutionState[event.index].copy(
                    beforeNum = event.value
                )
            }
            AddEvolutionViewModel.After -> {
                _evolutionState[event.index] = _evolutionState[event.index].copy(
                    afterNum = event.value
                )
            }
            AddEvolutionViewModel.Conditions -> {
                _evolutionState[event.index] = _evolutionState[event.index].copy(
                    evolutionConditions = event.value
                )
            }
            AddEvolutionViewModel.Type -> {
                _evolutionState[event.index] = _evolutionState[event.index].copy(
                    evolutionType = event.value
                )
            }
        }
    }

    private fun updateEvolution(
        resultListener: (String) -> Unit
    ) = viewModelScope.launch {
        numberList.addAll(_evolutionState.map { it.afterNum })
        numberList.addAll(_evolutionState.map { it.beforeNum })

        val numbers = numberList
            .distinct()
            .sorted()
            .reduce { result, new ->
                "$result,$new"
            }

        repository.updateEvolution(
            param = EvolutionUpdateParam(
                items = _evolutionState
                    .map { it.also { info -> info.numbers = numbers } }
                    .mapNotNull { it.mapper() },
                removeItems = removeIndexList
            ),
            successListener = {
                resultListener(it)
                numberList.clear()
            },
            failureListener = {
                resultListener("등록 실패")
            }
        )
    }

    sealed class Event {
        object Init: Event()
        object Loading: Event()
        object Complete: Event()
    }

}