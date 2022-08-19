package com.example.pokemondex.view.list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemondex.network.data.*
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

    private val _stateFlow = MutableStateFlow<Event>(Event.Init)
    val stateFlow : StateFlow<Event> = _stateFlow

    private val _pokemonList = mutableStateListOf<PokemonListItem>()
    val pokemonList: List<PokemonListItem> = _pokemonList

    private val _imageState = mutableStateOf(false)
    val imageState: State<Boolean> = _imageState

    private val _searchState = mutableStateOf("")
    val searchState : State<String> = _searchState

    private var _typeList = mutableStateListOf<ConditionPokemonType>().also {
        it.addAll(
            TypeInfo.values()
                .dropLast(1).map { info -> info.mapper() }
        )
    }
    val typeList: List<ConditionPokemonType> = _typeList
    var selectTypes = TypeInfo.values().map { it.koreanName }.dropLast(1).reduce{ total, new -> "$total,$new" }

    private var _generateList = mutableStateListOf<ConditionGenerate>().also {
        it.addAll(getConditionGenerateList())
    }
    val generateList: List<ConditionGenerate> = _generateList
    var selectGenerate = ""

    init {
        savedStateHandle.get<String>("group")?.let { generation ->
            selectGenerate = if (generation == "all") {
                "1,2,3,4,5,6,7,8,9"
            } else {
                generation
            }
            selectPokemonList()
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
                selectPokemonList()
            }
            is ListEvent.TypeCondition -> {
                typeConditionChange(event.typeIndex)
            }
            is ListEvent.GenerateCondition -> {
                generateConditionChange(event.index)
            }
            is ListEvent.SettingCondition -> {
                selectTypes = _typeList.filter { it.isSelect }.map { it.name }
                    .reduce{ total, new -> "$total,$new" }
                selectGenerate = _generateList.filter { it.isSelect }.map { it.generate }
                    .reduce{ total, new -> "$total,$new" }
                selectPokemonList()
            }
            is ListEvent.MenuOpen -> {
                settingMenuOpen()
            }
        }
    }

    private fun selectPokemonList() = viewModelScope.launch {
        _pokemonList.clear()
        _stateFlow.emit(Event.Init)

        repository.selectSearchPokemonList(
            searchInfo = SearchInfo(
                generations = getSelectGenerationList(),
                searchText = _searchState.value
            ),
            typeList = getSelectTypeList(),
            successListener = {
                _pokemonList.addAll(it)
            },
            failureListener = {
                _pokemonList.isEmpty()
            }
        )

        _stateFlow.emit(Event.Complete)
    }

    private fun getSelectGenerationList() =
        selectGenerate.split(",").map { it.toInt() }

    private fun getSelectTypeList(): List<String> =
        selectTypes.split(",")

    private fun settingMenuOpen() {
        val typeTemp = _typeList.mapIndexed { index, type ->
            _typeList[index].copy(
                isSelect = selectTypes.contains(type.name)
            )
        }
        val generateTemp = _generateList.mapIndexed { index, generate ->
            _generateList[index].copy(
                isSelect = selectGenerate.contains(generate.generate)
            )
        }

        _typeList.clear()
        _typeList.addAll(typeTemp)
        _generateList.clear()
        _generateList.addAll(generateTemp)
    }

    private fun generateConditionChange(index: Int) {
        if (index == -1) {
            val changeValue = _generateList.map { it.isSelect }.none { it.not() }.not()

            val temp = _generateList.mapIndexed { i, _ ->
                _generateList[i].copy(isSelect = changeValue)
            }
            _generateList.clear()
            _generateList.addAll(temp)

            return
        }
        _generateList[index] = _generateList[index].copy(isSelect = _generateList[index].isSelect.not())
    }

    private fun typeConditionChange(index: Int) {
        if (index == -1) {
            val changeValue = _typeList.map { it.isSelect }.none { it.not() }.not()

            val temp = _typeList.mapIndexed { i, _ ->
                _typeList[i].copy(isSelect = changeValue)
            }
            _typeList.clear()
            _typeList.addAll(temp)

            return
        }
        _typeList[index] = _typeList[index].copy(isSelect = _typeList[index].isSelect.not())
    }

    sealed class Event {
        object Init: Event()
        object Complete: Event()
    }

}