package com.example.pokemondex.util

fun String.toIntOrZero() = try {
    toInt()
} catch (e: Exception) {
    0
}