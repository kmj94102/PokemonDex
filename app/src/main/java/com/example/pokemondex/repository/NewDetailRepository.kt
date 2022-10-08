package com.example.pokemondex.repository

import com.example.pokemondex.database.DatabaseClient
import com.example.pokemondex.database.PokemonButtonInfo
import com.example.pokemondex.network.data.Evolution
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

class NewDetailRepository @Inject constructor(
    private val client: DatabaseClient
) {

    suspend fun selectButtonsInfo(
        number: Long,
        result: (PokemonButtonInfo?, PokemonButtonInfo?) -> Unit
    ) {
        val beforeInfo = CoroutineScope(Dispatchers.Default).async {
            client.selectButtonInfo(number - 1)
        }
        val afterInfo = CoroutineScope(Dispatchers.Default).async {
            client.selectButtonInfo(number + 1)
        }

        result(beforeInfo.await(), afterInfo.await())
    }

    suspend fun selectEvolutionInfo(
        allDexNumber: String,
    ): List<Evolution> {
        val evolutionInfo = client.selectEvolutionInfo(
            allDexNumber = "%$allDexNumber%",
        )

        return evolutionInfo.mapNotNull {
            it?.let {
                val beforeInfo = client.selectPokemonImage(
                    it.beforeNum
                ) ?: return@let null

                val afterInfo = client.selectPokemonImage(
                    it.afterNum
                ) ?: return@let null

                Evolution(
                    beforeDot = beforeInfo.image,
                    beforeShinyDot = beforeInfo.shinyImage,
                    afterDot = afterInfo.image,
                    afterShinyDot = afterInfo.shinyImage,
                    evolutionImage = it.image,
                    evolutionConditions = it.evolutionConditions
                )
            }
        }

    }

    fun selectPokemonInfo(number: String) = client.selectPokemonInfo(number)

    suspend fun updateNormal(
        number: String,
        normal: Boolean,
        successListener: (Unit) -> Unit,
        failureListener: () -> Unit
    ) {
        client.updateNormal(
            number = number,
            normal = normal,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    suspend fun updateShiny(
        number: String,
        shiny: Boolean,
        successListener: (Unit) -> Unit,
        failureListener: () -> Unit
    ) {
        client.updateShiny(
            number = number,
            shiny = shiny,
            successListener = successListener,
            failureListener = failureListener
        )
    }

    suspend fun updateImportance(
        number: String,
        importance: Boolean,
        successListener: (Unit) -> Unit,
        failureListener: () -> Unit
    ) {
        client.updateImportance(
            number = number,
            importance = importance,
            successListener = successListener,
            failureListener = failureListener
        )
    }

}