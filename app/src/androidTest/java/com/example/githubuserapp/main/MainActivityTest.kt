package com.example.githubuserapp.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.githubuserapp.R
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testFavoriteMenu() {
        // Klik menu favorite
        onView(withId(R.id.favorite))
            .check(matches(isDisplayed()))
            .perform(click())

        // Memastikan bahwa recycle view favorite user berhasil ditampilkan
        onView(withId(R.id.rv_favorite_user))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testRecyclerViewItem() {
        onView(withId(R.id.rv_user))
            .check(matches(isDisplayed()))

        // Klik item pertama pada recycle view
        onView(withId(R.id.rv_user))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Memastikan bahwa detail user berhasil ditampilkan (jumlah followers)
        onView(withId(R.id.tv_followers_num))
            .check(matches(isDisplayed()))
    }



}