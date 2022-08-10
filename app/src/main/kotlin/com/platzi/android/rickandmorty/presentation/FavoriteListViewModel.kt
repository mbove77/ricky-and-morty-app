package com.platzi.android.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.platzi.android.rickandmorty.database.CharacterDao
import com.platzi.android.rickandmorty.database.CharacterEntity
import com.platzi.android.rickandmorty.usecases.GetAllFavoriteCharactersUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Martín Bove on 30/7/2022.
 * E-mail: mbove77@gmail.com
 */
class FavoriteListViewModel(
    private val getAllFavoriteCharactersUseCase: GetAllFavoriteCharactersUseCase
): ViewModel() {

    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<FavoriteListNavigation>>()
    val events: LiveData<Event<FavoriteListNavigation>> get() = _events

    private val _favoriteCharacterList: LiveData<List<CharacterEntity>> get() =
        LiveDataReactiveStreams.fromPublisher(
            getAllFavoriteCharactersUseCase.invoke()
        )
    val favoriteCharacterList: LiveData<List<CharacterEntity>> get() = _favoriteCharacterList

    fun onFavoriteCharacterList(characterList: List<CharacterEntity>) {
        if (characterList.isEmpty()) {
            _events.value = Event(FavoriteListNavigation.ShowEmptyList)
            return
        }

        _events.value = Event(FavoriteListNavigation.ShowCharacterList(characterList))
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    sealed class FavoriteListNavigation {
        data class ShowCharacterList(val characterList: List<CharacterEntity>): FavoriteListNavigation()
        object ShowEmptyList: FavoriteListNavigation()
    }
}