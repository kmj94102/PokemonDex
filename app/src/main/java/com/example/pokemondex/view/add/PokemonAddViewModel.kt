package com.example.pokemondex.view.add

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemondex.network.data.Characteristic
import com.example.pokemondex.network.data.Pokemon
import com.example.pokemondex.network.data.PokemonStatusInfo
import com.example.pokemondex.network.data.StatusInfo
import com.example.pokemondex.repository.AddRepository
import com.example.pokemondex.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonAddViewModel @Inject constructor(
    private val repository: AddRepository
) : ViewModel() {

    private val _pokemonState = mutableStateOf(Pokemon())
    val pokemonState: State<Pokemon> = _pokemonState

    private val _characteristicList = mutableStateListOf(Characteristic())
    val characteristicList: List<Characteristic> = _characteristicList

    private val _typeList = mutableStateListOf("", "")
    val typeList: List<String> = _typeList

    private val _statusList = mutableStateListOf(
        PokemonStatusInfo(StatusInfo.HP.koreanName, 0),
        PokemonStatusInfo(StatusInfo.ATTACK.koreanName, 0),
        PokemonStatusInfo(StatusInfo.DEFENSE.koreanName, 0),
        PokemonStatusInfo(StatusInfo.SPECIAL_ATTACK.koreanName, 0),
        PokemonStatusInfo(StatusInfo.SPECIAL_DEFENSE.koreanName, 0),
        PokemonStatusInfo(StatusInfo.SPEED.koreanName, 0),
    )
    val statusList: List<PokemonStatusInfo> = _statusList

    fun event(event: AddEvent) {
        when (event) {
            is AddEvent.InputTextChange -> {
                changeInputText(event)
            }
            is AddEvent.InputCharacteristic -> {
                if (event.code == Characteristic) {
                    _characteristicList[event.index] = _characteristicList[event.index].copy(
                        name = event.value
                    )
                } else {
                    _characteristicList[event.index] = _characteristicList[event.index].copy(
                        description = event.value
                    )
                }
            }
            is AddEvent.AddCharacteristic -> {
                _characteristicList.add(Characteristic())
            }
            is AddEvent.RemoveCharacteristic -> {
                _characteristicList.removeLastOrNull()

            }
            is AddEvent.InputType -> {
                _typeList[event.index] = event.value
            }
            is AddEvent.DefaultImageSetting -> {
                _pokemonState.value = _pokemonState.value.copy(
                    dotImage = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/1.gif",
                    dotShinyImage = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/shiny/1.gif",
                    image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/1.png",
                    shinyImage = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/shiny/1.png"
                )
            }
            is AddEvent.Register -> {
                registerPokemon(
                    successListener = event.successListener,
                    failureListener = event.failureListener
                )
            }
            is AddEvent.SearchPokemonInfo -> {
                searchPokemonInfo(
                    index = event.index,
                    failureListener = event.failureListener
                )
            }
        }
    }

    private fun changeInputText(event: AddEvent.InputTextChange) {
        when (event.code) {
            Name -> {
                _pokemonState.value = _pokemonState.value.copy(name = event.value)
            }
            Number -> {
                _pokemonState.value = _pokemonState.value.copy(number = event.value)
            }
            Classification -> {
                _pokemonState.value = _pokemonState.value.copy(classification = event.value)
            }
            Description -> {
                _pokemonState.value = _pokemonState.value.copy(description = event.value)
            }
            Generation -> {
                _pokemonState.value =
                    _pokemonState.value.copy(generation = event.value.toIntOrNull())
            }
            StatusInfo.HP.originalName -> {
                _statusList[0] = _statusList[0].copy(value = event.value.toIntOrZero())
            }
            StatusInfo.ATTACK.originalName -> {
                _statusList[1] = _statusList[1].copy(value = event.value.toIntOrZero())
            }
            StatusInfo.DEFENSE.originalName -> {
                _statusList[2] = _statusList[2].copy(value = event.value.toIntOrZero())
            }
            StatusInfo.SPECIAL_ATTACK.originalName -> {
                _statusList[3] = _statusList[3].copy(value = event.value.toIntOrZero())
            }
            StatusInfo.SPECIAL_DEFENSE.originalName -> {
                _statusList[4] = _statusList[4].copy(value = event.value.toIntOrZero())
            }
            StatusInfo.SPEED.originalName -> {
                _statusList[5] = _statusList[5].copy(value = event.value.toIntOrZero())
            }
            Image -> {
                _pokemonState.value = _pokemonState.value.copy(image = event.value)
            }
            ShinyImage -> {
                _pokemonState.value = _pokemonState.value.copy(shinyImage = event.value)
            }
            DotImage -> {
                _pokemonState.value = _pokemonState.value.copy(dotImage = event.value)
            }
            DotShinyImage -> {
                _pokemonState.value = _pokemonState.value.copy(dotShinyImage = event.value)
            }
            else -> {}
        }
    }

    private fun searchPokemonInfo(
        index: String,
        failureListener: (String) -> Unit
    ) = viewModelScope.launch {
        repository.getPokemonInfo(
            index = index.toIntOrZero(),
            successListener = { infoResult, speciesInfo ->
                initSettings()

                infoResult.typeList.forEachIndexed { index, type ->
                    _typeList[index] = type
                }

                _characteristicList.clear()
                infoResult.abilities.forEachIndexed { index, ability ->
                    _characteristicList.add(Characteristic())
                    getAbility(ability, index)
                }

                setStatus(infoResult.status)

                _pokemonState.value = _pokemonState.value.copy(
                    number = index.padStart(4, '0'),
                    name = speciesInfo.name,
                    description = speciesInfo.description,
                    classification = speciesInfo.classification,
                    image = getPokemonImage(index.toIntOrZero()),
                    shinyImage = getPokemonShinyImage(index.toIntOrZero()),
                    dotImage = getPokemonDotImage(index.toIntOrZero()),
                    dotShinyImage = getPokemonDotShinyImage(index.toIntOrZero()),
                )
            },
            failureListener = {
                failureListener("조회 실패하였습니다.")
            }
        )
    }

    private fun getAbility(ability: String, index: Int) = viewModelScope.launch {
        repository.getAbilityInfo(
            name = ability,
            successListener = {
                _characteristicList[index] = _characteristicList[index].copy(
                    name = it.name,
                    description = it.description
                )
            },
            failureListener= {
                _characteristicList[index] = _characteristicList[index].copy(
                    name = null,
                    description = null
                )
            }
        )
    }

    private fun registerPokemon(
        successListener: (String) -> Unit,
        failureListener: () -> Unit
    ) = viewModelScope.launch {
        val pokemon = _pokemonState.value.copy(
            number = _pokemonState.value.number?.padStart(4, '0'),
            characteristic = setCharacteristic(),
            attribute = setAttribute(),
            status = setStatus()
        )

        if (pokemon.number.isNullOrEmpty() || pokemon.name.isNullOrEmpty()
            || pokemon.status.isNullOrEmpty() || pokemon.status == "0,0,0,0,0,0"
            || pokemon.description.isNullOrEmpty() || pokemon.characteristic.isNullOrEmpty()
            || pokemon.characteristic.isNullOrEmpty() || pokemon.attribute.isNullOrEmpty()
            || pokemon.image.isNullOrEmpty() || pokemon.shinyImage.isNullOrEmpty()
            || pokemon.dotImage.isNullOrEmpty() || pokemon.dotShinyImage.isNullOrEmpty()
            || _characteristicList.isEmpty()
        ) {
            failureListener()
            return@launch
        }

        var result = ""

        repository.insertPokemon(
            pokemon = pokemon,
            successListener = {
                result += it
            },
            failureListener = failureListener
        )

        _characteristicList.forEach { characteristic ->
            repository.insertCharacteristic(
                characteristic = characteristic,
                successListener = {
                    result += "\n$it"
                },
                failureListener = failureListener
            )
        }

        successListener(result)
    }

    private fun setAttribute() = try {
        _typeList
            .filter { it.isNotEmpty() }
            .reduce { result, new ->
                "$result,$new"
            }
    } catch (e: Exception) {
        ""
    }

    private fun setCharacteristic() = try {
        _characteristicList
            .filter { it.name?.isNotEmpty() == true && it.description?.isNotEmpty() == true }
            .map { it.name }
            .reduce { result, new ->
                "$result,$new"
            }
    } catch (e: Exception) {
        ""
    }

    private fun setStatus() = try {
        _statusList
            .map { it.value.toString() }
            .reduce { result, new ->
                "$result,$new"
            }
    } catch (e: Exception) {
        ""
    }

    private fun setStatus(map: Map<String, Int>) {
        _statusList[0] = PokemonStatusInfo(
            name = StatusInfo.HP.koreanName,
            value = map[StatusInfo.HP.koreanName] ?: 0
        )
        _statusList[1] = PokemonStatusInfo(
            name = StatusInfo.ATTACK.koreanName,
            value = map[StatusInfo.ATTACK.koreanName] ?: 0
        )
        _statusList[2] = PokemonStatusInfo(
            name = StatusInfo.DEFENSE.koreanName,
            value = map[StatusInfo.DEFENSE.koreanName] ?: 0
        )
        _statusList[3] = PokemonStatusInfo(
            name = StatusInfo.SPECIAL_ATTACK.koreanName,
            value = map[StatusInfo.SPECIAL_ATTACK.koreanName] ?: 0
        )
        _statusList[4] = PokemonStatusInfo(
            name = StatusInfo.SPECIAL_DEFENSE.koreanName,
            value = map[StatusInfo.SPECIAL_DEFENSE.koreanName] ?: 0
        )
        _statusList[5] = PokemonStatusInfo(
            name = StatusInfo.SPEED.koreanName,
            value = map[StatusInfo.SPEED.koreanName] ?: 0
        )
    }

    private fun initSettings() {
        _typeList[0] = ""
        _typeList[1] = ""

        _statusList[0] = PokemonStatusInfo(StatusInfo.HP.originalName, 0)
        _statusList[1] = PokemonStatusInfo(StatusInfo.ATTACK.originalName, 0)
        _statusList[2] = PokemonStatusInfo(StatusInfo.DEFENSE.originalName, 0)
        _statusList[3] = PokemonStatusInfo(StatusInfo.SPECIAL_ATTACK.originalName, 0)
        _statusList[4] = PokemonStatusInfo(StatusInfo.SPECIAL_DEFENSE.originalName, 0)
        _statusList[5] = PokemonStatusInfo(StatusInfo.SPEED.originalName, 0)

        _pokemonState.value = Pokemon()
    }

    companion object {
        const val Name = "name"
        const val Number = "number"
        const val Description = "description"
        const val Characteristic = "characteristic"
        const val Classification = "classification"
        const val DotImage = "dotImage"
        const val DotShinyImage = "DotShinyImage"
        const val Image = "image"
        const val ShinyImage = "shinyImage"
        const val Generation = "generation"
    }

}