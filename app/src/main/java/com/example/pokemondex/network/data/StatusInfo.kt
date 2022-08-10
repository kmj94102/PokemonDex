package com.example.pokemondex.network.data

import androidx.compose.ui.graphics.Color

enum class StatusInfo(
    val originalName: String,
    val koreanName: String,
    val color: Color
) {
    HP(originalName = "hp", koreanName = "HP", Color(0xFFEF5350)),
    ATTACK(originalName = "attack", koreanName = "공격", Color(0xFFFF7043)),
    DEFENSE(originalName = "defense", koreanName = "방어", Color(0xFFFFCA28)),
    SPECIAL_ATTACK(originalName = "special-attack", koreanName = "특수공격", Color(0xFF42A5F5)),
    SPECIAL_DEFENSE(originalName = "special-defense", koreanName = "특수방어", Color(0xFF66BB6A)),
    SPEED(originalName = "speed", koreanName = "스피드", Color(0xFFEC407A));
}

fun getStatusKoreanName(originalName: String) =
    StatusInfo.values().firstOrNull { it.originalName == originalName }?.koreanName ?: "HP"

fun getStatusColor(koreanName: String) =
    StatusInfo.values().firstOrNull { it.koreanName == koreanName }?.color ?: Color(0xffffffff)

