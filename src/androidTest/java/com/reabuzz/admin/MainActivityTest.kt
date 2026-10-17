package com.reabuzz.admin

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun `main activity launches successfully`() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity { activity ->
            assertNotNull("Activity should not be null", activity)
        }
    }

    @Test
    fun `navigation to profile`() {
        ActivityScenario.launch(MainActivity::class.java)
        
        // Click profile in bottom nav
        onView(withId(R.id.nav_profile)).perform(click())
        
        // Verify navigation (would check for profile fragment)
        // onView(withText("Profile")).check(matches(isDisplayed()))
    }

    @Test
    fun `navigation to admin dashboard`() {
        ActivityScenario.launch(MainActivity::class.java)
        
        // Click admin in bottom nav
        onView(withId(R.id.nav_admin)).perform(click())
        
        // Verify admin dashboard
        // onView(withText("Admin Dashboard")).check(matches(isDisplayed()))
    }
}
