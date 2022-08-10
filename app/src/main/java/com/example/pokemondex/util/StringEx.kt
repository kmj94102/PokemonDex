package com.example.pokemondex.util

fun getPokemonDotImage(index: Int) =
    if (index < 650) "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/$index.gif"
    else if (index < 722) "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vi/x-y/$index.png"
    else "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$index.png"

fun getPokemonDotShinyImage(index: Int) =
    if (index < 650) "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/shiny/$index.gif"
    else if (index < 722) "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-vi/x-y/shiny/$index.png"
    else "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/$index.png"

fun getPokemonImage(index: Int) =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/$index.png"

fun getPokemonShinyImage(index: Int) =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/home/shiny/$index.png"