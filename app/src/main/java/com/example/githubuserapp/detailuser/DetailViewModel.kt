package com.example.githubuserapp.detailuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.api.ApiConfig
import com.example.githubuserapp.event.Event
import retrofit2.Call
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _userDetail = MutableLiveData<DetailUserResponse>()
    val userDetail: LiveData<DetailUserResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isConnectionFailed = MutableLiveData<Event<Boolean>>()
    val isConnectionFailed: LiveData<Event<Boolean>> = _isConnectionFailed

    private var username = ""

    fun findDetailUser(username: String) {
        if (this.username != username) {
            _isLoading.value = true
            _isConnectionFailed.value = Event(false)
        }

        this.username = username

        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : retrofit2.Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                } else {
                    _isConnectionFailed.value = Event(true)
                    //Kode untuk menampillkan pesan jika data tidak dapat ditampilkan kepada pengguna terletak pada file activity dan fragment
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isConnectionFailed.value = Event(true)
                //Kode untuk menampillkan pesan jika data tidak dapat ditampilkan kepada pengguna terletak pada file activity dan fragment
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}