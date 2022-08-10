package com.platzi.android.rickandmorty.usecases

import com.platzi.android.rickandmorty.database.CharacterDao
import io.reactivex.schedulers.Schedulers

/**
 * Created by Martín Bove on 10/8/2022.
 * E-mail: mbove77@gmail.com
 */
class GetAllFavoriteCharactersUseCase(private val characterDao: CharacterDao) {
    fun invoke() = characterDao
        .getAllFavoriteCharacters()
        .onErrorReturn { emptyList() }
        .subscribeOn(Schedulers.io())
}