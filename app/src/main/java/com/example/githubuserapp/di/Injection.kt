package com.example.githubuserapp.di

import android.content.Context
import com.example.githubuserapp.api.ApiConfig
import com.example.githubuserapp.favorite.FavoriteUserRepository
import com.example.githubuserapp.favorite.FavoriteUserRoomDatabase

object Injection {
    fun provideRepository(context: Context): FavoriteUserRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteUserRoomDatabase.getDatabase(context)
        val dao = database.favoriteUserDao()
        return FavoriteUserRepository.getInstance(apiService, dao)
    }
}