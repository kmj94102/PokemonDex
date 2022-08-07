package com.example.pokemondex

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PokemonDex : Application() {

    companion object {
        private lateinit var application: PokemonDex
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }

}