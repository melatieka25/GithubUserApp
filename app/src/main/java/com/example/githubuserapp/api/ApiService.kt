package com.example.githubuserapp.api

import com.example.githubuserapp.detailuser.DetailUserResponse
import com.example.githubuserapp.main.ItemsItem
import com.example.githubuserapp.main.UserListResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("search/users")
    fun getUserWithQuery(
        @Query("q") q: String
    ): Call<UserListResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}