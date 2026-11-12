package com.reabuzz.admin.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.reabuzz.admin.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Executes the real Compose UI on the JVM. Robolectric inflates the debug
 * variant's manifest, so the debug application class and its debug account are
 * the ones under test.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LoginFlowTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun launchesOnTheSignInScreen() {
        composeRule.onNodeWithTag("login_username").assertIsDisplayed()
        composeRule.onNodeWithTag("login_password").assertIsDisplayed()
        composeRule.onNodeWithTag("login_button").assertIsDisplayed()
    }

    @Test
    fun rejectsUnknownCredentials() {
        composeRule.onNodeWithTag("login_username").performTextInput("nobody")
        composeRule.onNodeWithTag("login_password").performTextInput("wrong")
        composeRule.onNodeWithTag("login_button").performClick()

        composeRule.onNodeWithTag("login_username").assertIsDisplayed()
    }

    @Test
    fun debugAccountSignsInAndSeesTheAdminPanel() {
        composeRule.onNodeWithTag("login_username").performTextInput("debug_admin")
        composeRule.onNodeWithTag("login_password").performTextInput("anything")
        composeRule.onNodeWithTag("login_button").performClick()

        composeRule.onNodeWithTag("home_username").assertIsDisplayed()
    }
}
