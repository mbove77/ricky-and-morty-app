package com.platzi.android.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.platzi.android.rickandmorty.api.CharacterServer
import com.platzi.android.rickandmorty.api.EpisodeServer
import com.platzi.android.rickandmorty.api.toCharacterEntity
import com.platzi.android.rickandmorty.database.CharacterEntity
import com.platzi.android.rickandmorty.presentation.CharacterDetailViewModel.CharacterDetailNavigation.*
import com.platzi.android.rickandmorty.usecases.GetEpisodeFromCharacterUseCase
import com.platzi.android.rickandmorty.usecases.UpdateFavoriteCharacterStatusUseCase
import com.platzi.android.rickandmorty.usecases.ValidateFavoriteCharaterStatusUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Martín Bove on 1/8/2022.
 * E-mail: mbove77@gmail.com
 */
class CharacterDetailViewModel(
    private val character: CharacterServer,
    private val validateFavoriteCharaterStatusUseCase: ValidateFavoriteCharaterStatusUseCase,
    private val getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase,
    private val updateFavoriteCharacterStatusUseCase: UpdateFavoriteCharacterStatusUseCase
): ViewModel() {
    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<CharacterDetailNavigation>>()
    val events: LiveData<Event<CharacterDetailNavigation>> get() = _events

    private val _characterValues = MutableLiveData<CharacterServer>()
    val characterValues: LiveData<CharacterServer> get() = _characterValues

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite


    fun onCharacterValidation() {
        if (character ==  null) {
            _events.value = Event(CloseActivity)
            return
        }
        _characterValues.value = character
        validateFavoriteCharacterStatus(character.id)
        requestShowEpisodeList(character.episodeList)
    }


    private fun validateFavoriteCharacterStatus(characterId: Int){
        disposable.add(
                validateFavoriteCharaterStatusUseCase.invoke(characterId)
                .subscribe { isFavorite ->
                   _isFavorite.value = isFavorite
                }
        )
    }

    private fun requestShowEpisodeList(episodeUrlList: List<String>){
        disposable.add(
                getEpisodeFromCharacterUseCase.invoke(episodeUrlList)
                .doOnSubscribe {
                   _events.value = Event(ShowEpisodeListLoading)
                }
                .subscribe(
                    { episodeList ->
                        _events.value = Event(HideEpisodeListLoading)
                        _events.value = Event(ShowEpisodeList(episodeList))
                    },
                    { error ->
                        _events.value = Event(HideEpisodeListLoading)
                        _events.value = Event(ShowEpisodeError(error))
                    })
        )
    }

    fun onUpdateFavoriteCharacterStatus() {
        val characterEntity: CharacterEntity = character!!.toCharacterEntity()
        disposable.add(
                updateFavoriteCharacterStatusUseCase.invoke(characterEntity)
                .subscribeOn(Schedulers.io())
                .subscribe { isFavorite ->
                   _isFavorite.value = isFavorite
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    sealed class CharacterDetailNavigation {
        data class ShowEpisodeError(val error: Throwable): CharacterDetailNavigation()
        data class ShowEpisodeList(val episodeList: List<EpisodeServer>): CharacterDetailNavigation()
        object HideEpisodeListLoading: CharacterDetailNavigation()
        object ShowEpisodeListLoading: CharacterDetailNavigation()
        object CloseActivity: CharacterDetailNavigation()
    }
}