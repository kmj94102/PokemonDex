package com.example.pokemondex.network.data

import com.example.pokemondex.R

enum class TypeInfo (val originalName: String, val koreanName: String, val imageRes: Int){
    Normal("Normal", "노말", R.drawable.img_normal),
    Fighting("fighting", "격투", R.drawable.img_fighting),
    Flying("flying", "비행", R.drawable.img_flying),
    Poison("poison", "독", R.drawable.img_poison),
    Ground("ground", "땅", R.drawable.img_ground),
    Rock("rock", "바위", R.drawable.img_rock),
    Bug("bug", "벌레", R.drawable.img_bug),
    Ghost("ghost", "고스트", R.drawable.img_ghost),
    Steel("steel", "강철", R.drawable.img_steel),
    Fire("fire", "불꽃", R.drawable.img_fire),
    Water("water", "물", R.drawable.img_water),
    Grass("grass", "풀", R.drawable.img_grass),
    Electric("electric", "전기", R.drawable.img_electric),
    Psychic("psychic", "에스퍼", R.drawable.img_psychic),
    Ice("ice", "얼을", R.drawable.img_ice),
    Dragon("dragon", "드래곤", R.drawable.img_dragon),
    Dark("dark", "악", R.drawable.img_dark),
    Fairy("fairy", "페어리", R.drawable.img_fairy),
    Unknown("unknown", "???", R.drawable.img_monsterbal)
}

fun getTypeKoreaName(originalName: String) =
    TypeInfo.values().find { it.originalName == originalName }?.koreanName ?: TypeInfo.Unknown.name

fun getTypeImage(koreanName: String) =
    TypeInfo.values().find { it.koreanName == koreanName }?.imageRes ?: TypeInfo.Unknown.imageRes
