package com.example.githubuserapp.main

import com.google.gson.annotations.SerializedName

data class UserListResponse(

    @field:SerializedName("total_count")
    val totalCount: Int? = null,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean? = null,

    @field:SerializedName("items")
    val items: List<ItemsItem?>? = null
)

data class ItemsItem(

    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @field:SerializedName("login")
    val login: String? = null,

    @field:SerializedName("url")
    val url: String? = null
)
