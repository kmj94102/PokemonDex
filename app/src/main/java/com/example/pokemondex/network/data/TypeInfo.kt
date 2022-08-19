package com.example.pokemondex.network.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.pokemondex.R

enum class TypeInfo (val originalName: String, val koreanName: String, val imageRes: Int, val color: Long){
    Normal("normal", "노말", R.drawable.img_normal, 0xFFE3E8ED),
    Fire("fire", "불꽃", R.drawable.img_fire, 0xFFFFC289),
    Water("water", "물", R.drawable.img_water, 0xFF7CC5FF),
    Electric("electric", "전기", R.drawable.img_electric, 0xFFFFF6A8),
    Grass("grass", "풀", R.drawable.img_grass, 0xFF91FBA0),
    Ice("ice", "얼음", R.drawable.img_ice, 0xFF96FCEF),
    Fighting("fighting", "격투", R.drawable.img_fighting, 0xFFFF93B6),
    Poison("poison", "독", R.drawable.img_poison, 0xFFCF9EFF),
    Ground("ground", "땅", R.drawable.img_ground, 0xFFFFB877),
    Flying("flying", "비행", R.drawable.img_flying, 0xFFB5D0FF),
    Psychic("psychic", "에스퍼", R.drawable.img_psychic, 0xFFFFA4AD),
    Bug("bug", "벌레", R.drawable.img_bug, 0xFFBBFF86),
    Rock("rock", "바위", R.drawable.img_rock, 0xFFEEDAA5),
    Ghost("ghost", "고스트", R.drawable.img_ghost, 0xFF9E8EFF),
    Dragon("dragon", "드래곤", R.drawable.img_dragon, 0xFF587DFF),
    Dark("dark", "악", R.drawable.img_dark, 0xFF918A9C),
    Steel("steel", "강철", R.drawable.img_steel, 0xFF93C7DB),
    Fairy("fairy", "페어리", R.drawable.img_fairy, 0xFFFDADFF),
    Unknown("unknown", "???", R.drawable.img_monsterbal, 0xFF000000)
}

fun getTypeKoreaName(originalName: String) =
    TypeInfo.values().find { it.originalName == originalName }?.koreanName ?: TypeInfo.Unknown.name

fun getTypeImage(koreanName: String) =
    TypeInfo.values().find { it.koreanName == koreanName }?.imageRes ?: TypeInfo.Unknown.imageRes

fun getTypeColor(koreanName: String) =
    TypeInfo.values().find { it.koreanName == koreanName }?.color ?: TypeInfo.Unknown.color

@Composable
fun getTypeColorList(typeList: List<String>): List<Color> {
    return if (typeList.isEmpty()) {
        listOf(Color(TypeInfo.Unknown.color), Color(TypeInfo.Unknown.color))
    } else if (typeList.size == 1) {
        listOf(Color(getTypeColor(typeList[0])), Color(getTypeColor(typeList[0])))
    } else {
        listOf(Color(getTypeColor(typeList[0])), Color(getTypeColor(typeList[1])))
    }
}

fun getWeaknessInfo(type: String): List<Float> {
    return when(type) {
        TypeInfo.Normal.koreanName -> {
            listOf(
                1f, 1f, 1f, 1f, 1f, 1f,
                2f, 1f, 1f, 1f, 1f, 1f,
                1f, 0f, 1f, 1f, 1f, 1f,
            )
        }
        TypeInfo.Fire.koreanName -> {
            listOf(
                1f, 0.5f, 2f, 1f, 0.5f, 0.5f,
                1f, 1f, 2f, 1f, 1f, 0.5f,
                2f, 1f, 1f, 1f, 0.5f, 0.5f,
            )
        }
        TypeInfo.Water.koreanName -> {
            listOf(
                1f, 0.5f, 0.5f, 2f, 2f, 0.5f,
                1f, 1f, 1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f, 0.5f, 1f,
            )
        }
        TypeInfo.Electric.koreanName -> {
            listOf(
                1f, 1f, 1f, 0.5f, 1f, 1f,
                1f, 1f, 2f, 0.5f, 1f, 1f,
                1f, 1f, 1f, 1f, 0.5f, 1f,
            )
        }
        TypeInfo.Grass.koreanName -> {
            listOf(
                1f, 2f, 0.5f, 0.5f, 0.5f, 2f,
                1f, 2f, 0.5f, 2f, 1f, 2f,
                1f, 1f, 1f, 1f, 1f, 1f,
            )
        }
        TypeInfo.Ice.koreanName -> {
            listOf(
                1f, 2f, 1f, 1f, 1f, 0.5f,
                2f, 1f, 1f, 1f, 1f, 1f,
                2f, 1f, 1f, 1f, 2f, 1f,
            )
        }
        TypeInfo.Fighting.koreanName -> {
            listOf(
                1f, 1f, 1f, 1f, 1f, 1f,
                1f, 1f, 1f, 2f, 2f, 0.5f,
                0.5f, 1f, 1f, 0.5f, 1f, 2f,
            )
        }
        TypeInfo.Poison.koreanName -> {
            listOf(
                1f, 1f, 1f, 1f, 0.5f, 1f,
                0.5f, 0.5f, 2f, 1f, 2f, 0.5f,
                1f, 1f, 1f, 1f, 1f, 0.5f,
            )
        }
        TypeInfo.Ground.koreanName -> {
            listOf(
                1f, 1f, 2f, 0.5f, 2f, 2f,
                1f, 0.5f, 1f, 1f, 1f, 1f,
                0.5f, 1f, 1f, 1f, 1f, 1f,
            )
        }
        TypeInfo.Flying.koreanName -> {
            listOf(
                1f, 1f, 1f, 2f, 0.5f, 2f,
                0.5f, 1f, 0f, 1f, 1f, 0.5f,
                2f, 1f, 1f, 1f, 1f, 1f,
            )
        }
        TypeInfo.Psychic.koreanName -> {
            listOf(
                1f, 1f, 1f, 1f, 1f, 1f,
                0.5f, 1f, 1f, 1f, 0.5f, 2f,
                1f, 2f, 1f, 2f, 1f, 1f,
            )
        }
        TypeInfo.Bug.koreanName -> {
            listOf(
                1f, 2f, 1f, 1f, 0.5f, 1f,
                0.5f, 1f, 0.5f, 2f, 1f, 1f,
                2f, 1f, 1f, 1f, 1f, 1f,
            )
        }
        TypeInfo.Rock.koreanName -> {
            listOf(
                0.5f, 0.5f, 2f, 1f, 2f, 1f,
                2f, 0.5f, 2f, 0.5f, 1f, 1f,
                1f, 1f, 1f, 1f, 2f, 1f,
            )
        }
        TypeInfo.Ghost.koreanName -> {
            listOf(
                0f, 1f, 1f, 1f, 1f, 1f,
                0f, 0.5f, 1f, 1f, 1f, 0.5f,
                1f, 2f, 1f, 2f, 1f, 1f,
            )
        }
        TypeInfo.Dragon.koreanName -> {
            listOf(
                1f, 0.5f, 0.5f, 0.5f, 0.5f, 2f,
                1f, 1f, 1f, 1f, 1f, 1f,
                1f, 1f, 2f, 1f, 1f, 2f,
            )
        }
        TypeInfo.Dark.koreanName -> {
            listOf(
                1f, 1f, 1f, 1f, 1f, 1f,
                2f, 1f, 1f, 1f, 0.5f, 2f,
                1f, 0.5f, 1f, 0.5f, 1f, 2f,
            )
        }
        TypeInfo.Steel.koreanName -> {
            listOf(
                0.5f, 2f, 1f, 1f, 0.5f, 0.5f,
                2f, 0.5f, 2f, 0.5f, 0.5f, 0.5f,
                0.5f, 1f, 0.5f, 1f, 0.5f, 0.5f,
            )
        }
        TypeInfo.Steel.koreanName -> {
            listOf(
                1f, 1f, 1f, 1f, 1f, 1f,
                0.5f, 2f, 1f, 1f, 1f, 0.5f,
                1f, 1f, 0f, 0.5f, 2f, 1f
            )
        }
        else -> {
            listOf(
                0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f, 0f, 0f
            )
        }
    }
}