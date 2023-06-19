package com.example.githubuserapp.favorite

import androidx.lifecycle.LiveData
import com.example.githubuserapp.api.ApiService

class FavoriteUserRepository private constructor(
    private val apiService: ApiService,
    private val favoriteUserDao: FavoriteUserDao
) {

    fun getFavoriteUsers(): LiveData<List<FavoriteUser>> {
        return favoriteUserDao.getAllFavoriteUser()
    }

    suspend fun insertFavoriteUser(favoriteUser: FavoriteUser) {
        favoriteUserDao.insert(favoriteUser)
    }

    suspend fun deleteFavoriteUser(username: String) {
        favoriteUserDao.deleteByUsername(username)
    }

    suspend fun isFavoriteUser(username: String): Boolean {
        return favoriteUserDao.isFavoriteUser(username)
    }

    companion object {
        @Volatile
        private var instance: FavoriteUserRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteUserDao: FavoriteUserDao
        ): FavoriteUserRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteUserRepository(apiService, favoriteUserDao)
            }.also { instance = it }
    }
}