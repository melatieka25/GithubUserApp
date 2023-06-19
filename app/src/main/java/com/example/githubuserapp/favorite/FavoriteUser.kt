package com.example.githubuserapp.favorite

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorite_user")
@Parcelize
data class FavoriteUser(
    @field:PrimaryKey(autoGenerate = false)
    var username: String = "",

    @field:ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null,

    @field:ColumnInfo(name = "url")
    var url: String? = null,
) : Parcelable
