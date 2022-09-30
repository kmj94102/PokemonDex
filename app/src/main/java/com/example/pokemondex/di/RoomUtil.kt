package com.example.pokemondex.di

import android.content.Context
import androidx.room.Room
import com.example.pokemondex.database.PokemonDao
import com.example.pokemondex.database.PokemonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomUtil {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): PokemonDatabase =
        Room.databaseBuilder(context, PokemonDatabase::class.java, PokemonDatabase.Database_Name)
            .build()

    @Provides
    @Singleton
    fun providePokemonDao(
        database: PokemonDatabase
    ): PokemonDao =
        database.pokemonDao()

}