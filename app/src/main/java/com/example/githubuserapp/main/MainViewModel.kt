package com.example.githubuserapp.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.api.ApiConfig
import com.example.githubuserapp.event.Event
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isConnectionFailed = MutableLiveData<Event<Boolean>>()
    val isConnectionFailed: LiveData<Event<Boolean>> = _isConnectionFailed

    init {
        findUser("Melati")
    }

    fun findUser(q: String) {
        _isConnectionFailed.value = Event(false)
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserWithQuery(q)
        client.enqueue(object : retrofit2.Callback<UserListResponse> {
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items as List<ItemsItem>?
                } else {
                    _isConnectionFailed.value = Event(true)
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                _isLoading.value = false
                _isConnectionFailed.value = Event(true)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}