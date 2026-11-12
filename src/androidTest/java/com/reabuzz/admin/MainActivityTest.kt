package com.reabuzz.admin

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun launchesOnTheSignInScreen() {
        composeRule.onNodeWithTag("login_username").assertIsDisplayed()
        composeRule.onNodeWithTag("login_button").assertIsDisplayed()
    }

    @Test
    fun rejectsUnknownCredentials() {
        composeRule.onNodeWithTag("login_username").performTextInput("nobody")
        composeRule.onNodeWithTag("login_password").performTextInput("wrong")
        composeRule.onNodeWithTag("login_button").performClick()

        // Still on the sign-in screen: the username field remains on screen.
        composeRule.onNodeWithTag("login_username").assertIsDisplayed()
    }

    @Test
    fun debugBuildSignsInWithTheDebugAccount() {
        // src/debug replaces the application class, so this account exists here.
        composeRule.onNodeWithTag("login_username").performTextInput("debug_admin")
        composeRule.onNodeWithTag("login_password").performTextInput("anything")
        composeRule.onNodeWithTag("login_button").performClick()

        composeRule.onNodeWithTag("home_username").assertIsDisplayed()
    }
}
