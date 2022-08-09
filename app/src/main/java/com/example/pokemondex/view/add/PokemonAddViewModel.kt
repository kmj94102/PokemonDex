package com.example.pokemondex.view.add

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemondex.network.data.Characteristic
import com.example.pokemondex.network.data.Pokemon
import com.example.pokemondex.network.data.PokemonStatusInfo
import com.example.pokemondex.repository.AddRepository
import com.example.pokemondex.util.toIntOrZero
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
        PokemonStatusInfo(Hp, 0),
        PokemonStatusInfo(Attack, 0),
        PokemonStatusInfo(Defence, 0),
        PokemonStatusInfo(SpecialAttack, 0),
        PokemonStatusInfo(SpecialDefence, 0),
        PokemonStatusInfo(Speed, 0),
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
            Hp -> {
                _statusList[0] = _statusList[0].copy(value = event.value.toIntOrZero())
            }
            Attack -> {
                _statusList[1] = _statusList[1].copy(value = event.value.toIntOrZero())
            }
            Defence -> {
                _statusList[2] = _statusList[2].copy(value = event.value.toIntOrZero())
            }
            SpecialAttack -> {
                _statusList[3] = _statusList[3].copy(value = event.value.toIntOrZero())
            }
            SpecialDefence -> {
                _statusList[4] = _statusList[4].copy(value = event.value.toIntOrZero())
            }
            Speed -> {
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
                    result += it
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

    companion object {
        const val Name = "name"
        const val Number = "number"
        const val Description = "description"
        const val Characteristic = "characteristic"
        const val Status = "status"
        const val Hp = "HP"
        const val Attack = "공격"
        const val Defence = "방어"
        const val SpecialAttack = "특공"
        const val SpecialDefence = "특방"
        const val Speed = "속도"
        const val Classification = "classification"
        const val Attribute = "attribute"
        const val DotImage = "dotImage"
        const val DotShinyImage = "DotShinyImage"
        const val Image = "image"
        const val ShinyImage = "shinyImage"
        const val Generation = "generation"
    }

}