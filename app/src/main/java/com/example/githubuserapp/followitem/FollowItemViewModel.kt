package com.example.githubuserapp.followitem

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.api.ApiConfig
import com.example.githubuserapp.event.Event
import com.example.githubuserapp.main.ItemsItem
import retrofit2.Call
import retrofit2.Response

class FollowItemViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userList = MutableLiveData<List<ItemsItem>>()
    val userList: LiveData<List<ItemsItem>> = _userList

    private val _isConnectionFailed = MutableLiveData<Event<Boolean>>()
    val isConnectionFailed: LiveData<Event<Boolean>> = _isConnectionFailed

    private var username = ""
    private var pageNum = ""

    fun findFollowItem(username: String, pageNum: String) {
        if (this.username != username || this.pageNum != pageNum) {
            _isConnectionFailed.value = Event(false)
            _isLoading.value = true
        }

        this.username = username
        this.pageNum = pageNum

        val client = when (pageNum) {
            "0" -> ApiConfig.getApiService().getUserFollowers(username)
            else -> ApiConfig.getApiService().getUserFollowing(username)
        }
        client.enqueue(object : retrofit2.Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    when (pageNum) {
                        "0" -> _userList.value = response.body()
                        else -> _userList.value = response.body()
                    }
                } else {
                    _isConnectionFailed.value = Event(true)
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _isConnectionFailed.value = Event(true)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "FollowItemViewModel"
    }
}