package com.example.pokemondex.network.data

import com.example.pokemondex.R

enum class TypeInfo (val originalName: String, val koreanName: String, val imageRes: Int){
    Normal("normal", "노말", R.drawable.img_normal),
    Fire("fire", "불꽃", R.drawable.img_fire),
    Water("water", "물", R.drawable.img_water),
    Electric("electric", "전기", R.drawable.img_electric),
    Grass("grass", "풀", R.drawable.img_grass),
    Ice("ice", "얼을", R.drawable.img_ice),
    Fighting("fighting", "격투", R.drawable.img_fighting),
    Poison("poison", "독", R.drawable.img_poison),
    Ground("ground", "땅", R.drawable.img_ground),
    Flying("flying", "비행", R.drawable.img_flying),
    Psychic("psychic", "에스퍼", R.drawable.img_psychic),
    Bug("bug", "벌레", R.drawable.img_bug),
    Rock("rock", "바위", R.drawable.img_rock),
    Ghost("ghost", "고스트", R.drawable.img_ghost),
    Dragon("dragon", "드래곤", R.drawable.img_dragon),
    Dark("dark", "악", R.drawable.img_dark),
    Steel("steel", "강철", R.drawable.img_steel),
    Fairy("fairy", "페어리", R.drawable.img_fairy),
    Unknown("unknown", "???", R.drawable.img_monsterbal)
}

fun getTypeKoreaName(originalName: String) =
    TypeInfo.values().find { it.originalName == originalName }?.koreanName ?: TypeInfo.Unknown.name

fun getTypeImage(koreanName: String) =
    TypeInfo.values().find { it.koreanName == koreanName }?.imageRes ?: TypeInfo.Unknown.imageRes

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