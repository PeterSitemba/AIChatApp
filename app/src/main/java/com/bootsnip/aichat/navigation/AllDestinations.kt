package com.bootsnip.aichat.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.bootsnip.aichat.navigation.AllDestinations.CHAT_HISTORY
import com.bootsnip.aichat.navigation.AllDestinations.HOME

object AllDestinations {
    const val HOME = "Home"
    const val CHAT_HISTORY = "ChatHistory"
}

class AppNavigationActions(private val navController: NavHostController) {

    fun navigateToHome() {
        navController.navigate(HOME) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToSettings() {
        navController.navigate(CHAT_HISTORY) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}