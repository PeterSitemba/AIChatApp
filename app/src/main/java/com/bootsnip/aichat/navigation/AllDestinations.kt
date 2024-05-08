package com.bootsnip.aichat.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.bootsnip.aichat.navigation.AllDestinations.CHAT_HISTORY_DETAIL
import com.bootsnip.aichat.navigation.AllDestinations.HOME

object AllDestinations {
    const val HOME = "Home"
    const val CHAT_HISTORY = "ChatHistory"
    const val CHAT_HISTORY_DETAIL = "ChatHistoryDetail"
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

    fun navigateToChatHistoryDetail() {
        navController.navigate(CHAT_HISTORY_DETAIL) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}