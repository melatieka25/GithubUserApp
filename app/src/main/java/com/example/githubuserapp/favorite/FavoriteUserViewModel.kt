package com.example.githubuserapp.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FavoriteUserViewModel(private val favoriteUserRepository: FavoriteUserRepository) :
    ViewModel() {
    private val _isFavoriteUser = MutableLiveData<Boolean>()
    val isFavoriteUser: LiveData<Boolean> = _isFavoriteUser

    fun getFavoriteUsers() = favoriteUserRepository.getFavoriteUsers()
    fun saveFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            favoriteUserRepository.insertFavoriteUser(favoriteUser)
        }
    }

    fun deleteFavoriteUser(username: String) {
        viewModelScope.launch {
            favoriteUserRepository.deleteFavoriteUser(username)
        }

    }

    fun isFavoriteUser(username: String) {
        viewModelScope.launch {
            _isFavoriteUser.value = favoriteUserRepository.isFavoriteUser(username)
        }
    }
}