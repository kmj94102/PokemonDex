package com.example.pokemondex.view.add

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemondex.network.data.EvolutionTemp
import com.example.pokemondex.repository.AddEvolutionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEvolutionViewModel @Inject constructor(
    private val repository: AddEvolutionRepository
) : ViewModel() {

    private val _evolutionInfoList = mutableStateListOf(EvolutionTemp())
    val evolutionInfoList: List<EvolutionTemp> = _evolutionInfoList

    private val _evolutionTypes = mutableStateListOf<String>()
    val evolutionTypes: List<String> = _evolutionTypes

    private val numberList: MutableList<String> = mutableListOf()

    init {
        selectEvolutionType()
    }

    fun event(event: AddEvolutionEvent) {
        when (event) {
            is AddEvolutionEvent.ImageSelect -> {
                getPokemonImage(event)
            }
            is AddEvolutionEvent.InputTextChange -> {
                setTextFiledChanged(event)
            }
            is AddEvolutionEvent.ListAdd -> {
                _evolutionInfoList.add(EvolutionTemp())
            }
            is AddEvolutionEvent.ListRemove -> {
                _evolutionInfoList.removeLastOrNull()
            }
            is AddEvolutionEvent.InsertEvolution -> {
                insertEvolution(event.resultListener)
            }
        }
    }

    private fun getPokemonImage(event: AddEvolutionEvent.ImageSelect) = viewModelScope.launch {
        val beforeNum = _evolutionInfoList[event.index].beforeNum
        val afterNum = _evolutionInfoList[event.index].afterNum

        if (beforeNum.isEmpty() || afterNum.isEmpty()) {
            event.failureListener("진화 전, 후의 번호를 먼저 입력해주세요.")
            return@launch
        }

        repository.getPokemonImage(
            number = beforeNum,
            successListener = {
                _evolutionInfoList[event.index] = _evolutionInfoList[event.index].copy(
                    beforeImage = it.dotImage
                )
            },
            failureListener = {
                event.failureListener("진화 전 이미지 조회를 실패하였습니다.")
            }
        )

        repository.getPokemonImage(
            number = afterNum,
            successListener = {
                _evolutionInfoList[event.index] = _evolutionInfoList[event.index].copy(
                    afterImage = it.dotImage
                )
            },
            failureListener = {
                event.failureListener("진화 후 이미지 조회를 실패하였습니다.")
            }
        )

    }

    private fun setTextFiledChanged(event: AddEvolutionEvent.InputTextChange) {
        when (event.code) {
            Before -> {
                _evolutionInfoList[event.index] = _evolutionInfoList[event.index].copy(
                    beforeNum = event.value
                )
            }
            After -> {
                _evolutionInfoList[event.index] = _evolutionInfoList[event.index].copy(
                    afterNum = event.value
                )
            }
            Conditions -> {
                _evolutionInfoList[event.index] = _evolutionInfoList[event.index].copy(
                    evolutionConditions = event.value
                )
            }
            Type -> {
                _evolutionInfoList[event.index] = _evolutionInfoList[event.index].copy(
                    evolutionType = event.value
                )
            }
        }
    }

    private fun selectEvolutionType() = viewModelScope.launch {
        repository.getEvolutionType(
            successListener = {
                _evolutionTypes.addAll(it)
                _evolutionTypes.distinct()
            },
            failureListener = { }
        )
    }

    private fun insertEvolution(
        resultListener: (String) -> Unit
    ) = viewModelScope.launch {
        numberList.addAll(_evolutionInfoList.map { it.afterNum })
        numberList.addAll(_evolutionInfoList.map { it.beforeNum })

        val numbers = numberList
            .distinct()
            .sorted()
            .reduce { result, new ->
                "$result,$new"
            }

        repository.insertEvolution(
            list = _evolutionInfoList
                .map { it.also { info -> info.numbers = numbers } }
                .mapNotNull { it.mapper() },
            successListener = {
                resultListener(it)
            },
            failureListener = {
                resultListener("등록 실패")
            }
        )
    }

    companion object {
        const val Before = "before"
        const val After = "after"
        const val Conditions = "conditions"
        const val Type = "type"
    }

}