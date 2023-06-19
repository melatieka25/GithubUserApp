package com.example.githubuserapp.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuserapp.followitem.FollowItemFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String = ""

    override fun getItemCount(): Int {
        return numOfPage
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowItemFragment()
        fragment.arguments = Bundle().apply {
            putString(FollowItemFragment.ARG_POSITION, position.toString())
            putString(FollowItemFragment.ARG_USERNAME, username)
        }
        return fragment
    }

    companion object {
        const val numOfPage = 2
    }


}