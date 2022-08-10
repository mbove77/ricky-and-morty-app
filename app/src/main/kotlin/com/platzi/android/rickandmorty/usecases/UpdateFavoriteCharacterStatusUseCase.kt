package com.platzi.android.rickandmorty.usecases

import com.platzi.android.rickandmorty.database.CharacterDao
import com.platzi.android.rickandmorty.database.CharacterEntity
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by Martín Bove on 10/8/2022.
 * E-mail: mbove77@gmail.com
 */
class UpdateFavoriteCharacterStatusUseCase(private val characterDao: CharacterDao) {
    fun invoke(characterEntity: CharacterEntity) = characterDao.getCharacterById(characterEntity.id)
        .isEmpty
        .flatMapMaybe { isEmpty ->
            if(isEmpty){
                characterDao.insertCharacter(characterEntity)
            }else{
                characterDao.deleteCharacter(characterEntity)
            }
            Maybe.just(isEmpty)
        }
        .observeOn(AndroidSchedulers.mainThread())
}