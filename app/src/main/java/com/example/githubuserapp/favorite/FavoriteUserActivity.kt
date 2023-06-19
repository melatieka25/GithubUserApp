package com.example.githubuserapp.favorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.adapter.ListFavoriteUserAdapter
import com.example.githubuserapp.databinding.ActivityFavoriteUserBinding
import com.example.githubuserapp.detailuser.DetailUserActivity
import com.example.githubuserapp.followitem.FollowItemFragment


class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding
    private val favoriteUserViewModel by viewModels<FavoriteUserViewModel> {
        FavoriteUserViewModelFactory.getInstance(
            this
        )
    }
    private var listFavoriteUser = ArrayList<FavoriteUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteUserViewModel.getFavoriteUsers().observe(this) { users ->
            setFavoriteUser(users)
        }

        binding.rvFavoriteUser.setHasFixedSize(true)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setFavoriteUser(favoriteUsers: List<FavoriteUser>) {
        this.listFavoriteUser = favoriteUsers as ArrayList<FavoriteUser>
        if (this.listFavoriteUser.size == 0) {
            binding.tvNoUserFav.text = getString(R.string.no_user_fav)
        }

        binding.rvFavoriteUser.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListFavoriteUserAdapter(listFavoriteUser)
        binding.rvFavoriteUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object :
            ListFavoriteUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FavoriteUser) {
                showSelectedUser(data.username)
            }
        })
    }

    private fun showSelectedUser(username: String?) {

        val moveToDetailUser = Intent(this@FavoriteUserActivity, DetailUserActivity::class.java)
        moveToDetailUser.putExtra(DetailUserActivity.EXTRA_USER, username)
        val mFragment = FollowItemFragment()
        val mBundle = Bundle()
        mBundle.putString(FollowItemFragment.ARG_USERNAME, username)
        mBundle.putString(FollowItemFragment.ARG_POSITION, "0")
        mFragment.arguments = mBundle
        startActivity(moveToDetailUser)
    }

    // Referensi: https://www.geeksforgeeks.org/how-to-add-and-customize-back-button-of-action-bar-in-android/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}