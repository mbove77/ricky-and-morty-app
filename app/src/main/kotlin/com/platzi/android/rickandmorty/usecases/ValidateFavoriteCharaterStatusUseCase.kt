package com.platzi.android.rickandmorty.usecases

import com.platzi.android.rickandmorty.database.CharacterDao
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Martín Bove on 10/8/2022.
 * E-mail: mbove77@gmail.com
 */
class ValidateFavoriteCharaterStatusUseCase(private val characterDao: CharacterDao) {
    fun invoke(characterId: Int) = characterDao.getCharacterById(characterId)
        .isEmpty
        .flatMapMaybe { isEmpty ->
            Maybe.just(!isEmpty)
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
}