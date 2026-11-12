package com.reabuzz.admin

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The admin tab is only rendered when the policy grants VIEW_ADMIN_PANEL, so an
 * account without the role must never reach the dashboard.
 */
@RunWith(AndroidJUnit4::class)
class AdminDashboardTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun adminSeesTheDashboardAfterSignIn() {
        composeRule.onNodeWithTag("login_username").performTextInput("debug_admin")
        composeRule.onNodeWithTag("login_password").performTextInput("x")
        composeRule.onNodeWithTag("login_button").performClick()

        composeRule.onNodeWithText("Admin").assertIsDisplayed()
        composeRule.onNodeWithText("Admin").performClick()
        composeRule.onNodeWithText("Administrator panel").assertIsDisplayed()
        composeRule.onNodeWithText("VIP panel").assertIsDisplayed()
    }
}
