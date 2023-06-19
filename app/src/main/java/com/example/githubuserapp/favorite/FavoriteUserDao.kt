package com.example.githubuserapp.favorite

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoriteUser: FavoriteUser)

    @Query("DELETE from favorite_user WHERE username = :username")
    suspend fun deleteByUsername(username: String)

    @Query("SELECT * from favorite_user ORDER BY username ASC")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT EXISTS (SELECT * from favorite_user WHERE username = :username)")
    suspend fun isFavoriteUser(username: String): Boolean
}