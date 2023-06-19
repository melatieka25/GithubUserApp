package com.example.githubuserapp.detailuser

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.R
import com.example.githubuserapp.adapter.SectionsPagerAdapter
import com.example.githubuserapp.databinding.ActivityDetailUserBinding
import com.example.githubuserapp.favorite.FavoriteUser
import com.example.githubuserapp.favorite.FavoriteUserViewModel
import com.example.githubuserapp.favorite.FavoriteUserViewModelFactory
import com.example.githubuserapp.followitem.FollowItemFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private val favoriteUserViewModel by viewModels<FavoriteUserViewModel> {
        FavoriteUserViewModelFactory.getInstance(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra(EXTRA_USER)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        if (username != null) {
            sectionsPagerAdapter.username = username
            supportActionBar?.title = username
        }

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView2, FollowItemFragment())
            .addToBackStack(null)
            .commit()

        if (username != null) {
            detailViewModel.findDetailUser(username)
        }

        detailViewModel.userDetail.observe(this) { userDetail ->
            setUserDetailData(userDetail)
        }


        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.isConnectionFailed.observe(this) {
            it.getContentIfNotHandled()?.let { isConnectionFailed ->
                showConnectionFailedToast(isConnectionFailed)
            }
        }
    }

    private fun setUserDetailData(userDetail: DetailUserResponse) {
        with(binding) {
            tvUsername.text = userDetail.login
            tvName.text = userDetail.name
            tvFollowersNum.text = userDetail.followers.toString()
            tvFollowingNum.text = userDetail.following.toString()
            profileImg.loadImage(userDetail.avatarUrl)
        }
        username = userDetail.login.toString()
        photo_url = userDetail.avatarUrl.toString()
        url = userDetail.url.toString()
        favoriteUserViewModel.isFavoriteUser(username)
        favoriteUserViewModel.isFavoriteUser.observe(this) {
            if (!it) {
                optionsMenu.findItem(R.id.favorite).icon =
                    ContextCompat.getDrawable(this, R.drawable.baseline_favorite_border_black_24)
            } else {
                optionsMenu.findItem(R.id.favorite).icon =
                    ContextCompat.getDrawable(this, R.drawable.baseline_favorite_black_24)
            }
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showConnectionFailedToast(isConnectionFailed: Boolean) {
        if (isConnectionFailed) {
            Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show()
        }
    }

    fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .into(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        if (menu != null) {
            optionsMenu = menu
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.favorite -> {
                favoriteUserViewModel.isFavoriteUser(username)
                favoriteUserViewModel.isFavoriteUser.observe(this) {
                    handleUserFavoriteStatus(it)
                    if (it) {
                        item.icon = ContextCompat.getDrawable(
                            this,
                            R.drawable.baseline_favorite_border_black_24
                        )
                    } else {
                        item.icon =
                            ContextCompat.getDrawable(this, R.drawable.baseline_favorite_black_24)
                    }
                }
            }
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleUserFavoriteStatus(isFavorite: Boolean) {
        if (isFavorite) {
            favoriteUserViewModel.deleteFavoriteUser(username)
        } else {
            val favoriteUser = FavoriteUser(username, photo_url, url)
            favoriteUserViewModel.saveFavoriteUser(favoriteUser)
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        var username = "username"
        var photo_url = "photo_url"
        var url = "url"
        lateinit var optionsMenu: Menu

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}