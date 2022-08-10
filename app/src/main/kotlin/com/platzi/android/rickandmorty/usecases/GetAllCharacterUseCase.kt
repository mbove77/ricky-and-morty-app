package com.platzi.android.rickandmorty.usecases

import com.platzi.android.rickandmorty.api.CharacterRequest
import com.platzi.android.rickandmorty.api.CharacterResponseServer
import com.platzi.android.rickandmorty.api.CharacterService
import com.platzi.android.rickandmorty.api.toCharacterServerList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Martín Bove on 10/8/2022.
 * E-mail: mbove77@gmail.com
 */
class GetAllCharacterUseCase(private val characterRequest: CharacterRequest) {
    fun invoke(currentPage: Int) = characterRequest
        .getService<CharacterService>()
        .getAllCharacters(currentPage)
        .map(CharacterResponseServer::toCharacterServerList)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
}