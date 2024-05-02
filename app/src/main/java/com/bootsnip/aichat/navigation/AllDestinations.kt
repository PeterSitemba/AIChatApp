package com.bootsnip.aichat.navigation

import androidx.navigation.NavHostController
import com.bootsnip.aichat.navigation.AllDestinations.HOME
import com.bootsnip.aichat.navigation.AllDestinations.SETTINGS

object AllDestinations {
    const val HOME = "Home"
    const val SETTINGS = "Settings"
}

class AppNavigationActions(private val navController: NavHostController) {

    fun navigateToHome() {
        navController.navigate(HOME) {
            popUpTo(HOME)
        }
    }

    fun navigateToSettings() {
        navController.navigate(SETTINGS) {
            launchSingleTop = true
            restoreState = true
        }
    }
}