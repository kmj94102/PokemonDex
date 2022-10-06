package com.example.pokemondex.util

import android.content.Context
import android.widget.Toast
import com.example.pokemondex.network.data.TypeInfo
import com.example.pokemondex.network.data.getWeaknessInfo

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.toast(msgRes: Int) {
    Toast.makeText(this, getString(msgRes), Toast.LENGTH_SHORT).show()
}

fun typeCompatibilityInfo(attribute: String): List<Pair<Float, String>> {
    val tempList = attribute.split(",").map { getWeaknessInfo(it) }

    if (tempList.isEmpty()) return emptyList()

    return tempList.reduce { acc, list ->
        acc.zip(list).map { it.first * it.second }
    }.zip(TypeInfo.values().map { it.koreanName }).sortedByDescending { it.first }
}