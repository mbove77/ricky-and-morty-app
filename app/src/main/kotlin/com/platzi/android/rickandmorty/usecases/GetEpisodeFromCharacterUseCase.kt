package com.platzi.android.rickandmorty.usecases

import com.platzi.android.rickandmorty.api.EpisodeRequest
import com.platzi.android.rickandmorty.api.EpisodeService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Martín Bove on 10/8/2022.
 * E-mail: mbove77@gmail.com
 */
class GetEpisodeFromCharacterUseCase(private val episodeRequest: EpisodeRequest) {
    fun invoke(episodeUrlList: List<String>) = Observable.fromIterable(episodeUrlList)
        .flatMap { episode: String ->
            episodeRequest.baseUrl = episode
            episodeRequest
                .getService<EpisodeService>()
                .getEpisode()
                .toObservable()
        }
        .toList()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
}