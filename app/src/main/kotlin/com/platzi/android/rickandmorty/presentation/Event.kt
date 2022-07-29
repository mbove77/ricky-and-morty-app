package com.platzi.android.rickandmorty.presentation

/**
 * Created by Martín Bove on 28/7/2022.
 * E-mail: mbove77@gmail.com
 */
data class Event<out T> (private val content: T) {
    private var hasBenHandled = false

    fun getContentIfNotHandled(): T?  = if (hasBenHandled) {
        null
    } else {
        hasBenHandled = true
        content
    }

}