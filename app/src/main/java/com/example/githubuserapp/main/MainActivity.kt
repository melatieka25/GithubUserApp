package com.example.githubuserapp.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.adapter.ListUserAdapter
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.detailuser.DetailUserActivity
import com.example.githubuserapp.favorite.FavoriteUserActivity
import com.example.githubuserapp.followitem.FollowItemFragment
import com.example.githubuserapp.setting.SettingPreferences
import com.example.githubuserapp.setting.SettingViewModel
import com.example.githubuserapp.setting.SettingViewModelFactory
import com.example.githubuserapp.setting.ThemeSettingActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private var listUser = ArrayList<ItemsItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        setContentView(binding.root)

        mainViewModel.listUser.observe(this) { users ->
            setUserData(users)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.isConnectionFailed.observe(this) {
            it.getContentIfNotHandled()?.let { isConnectionFailed ->
                showConnectionFailedToast(isConnectionFailed)
            }
        }

        binding.rvUser.setHasFixedSize(true)
    }

    private fun setUserData(users: List<ItemsItem>) {
        print(users)
        this.listUser = users as ArrayList<ItemsItem>
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(listUser)
        binding.rvUser.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showSelectedUser(data.login)
            }
        })
    }

    private fun showSelectedUser(username: String?) {

        val moveToDetailUser = Intent(this@MainActivity, DetailUserActivity::class.java)
        moveToDetailUser.putExtra(DetailUserActivity.EXTRA_USER, username)
        val mFragment = FollowItemFragment()
        val mBundle = Bundle()
        mBundle.putString(FollowItemFragment.ARG_USERNAME, username)
        mBundle.putString(FollowItemFragment.ARG_POSITION, "0")
        mFragment.arguments = mBundle
        startActivity(moveToDetailUser)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.search -> {
                val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
                val searchView = item.actionView as SearchView

                searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
                searchView.queryHint = resources.getString(R.string.search_hint)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query != null) {
                            mainViewModel.findUser(query)
                        }
                        searchView.clearFocus()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
            }
            R.id.setting -> {
                val moveToSetting = Intent(this@MainActivity, ThemeSettingActivity::class.java)
                startActivity(moveToSetting)
            }
            R.id.favorite -> {
                val moveToFavorite = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                startActivity(moveToFavorite)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showConnectionFailedToast(isConnectionFailed: Boolean) {
        if (isConnectionFailed) {
            Toast.makeText(
                this,
                "Pengambilan data gagal. Harap periksa koneksi pada perangkatmu!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}