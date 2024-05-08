package com.bootsnip.aichat.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bootsnip.aichat.R
import com.bootsnip.aichat.ui.screens.ChatHistoryDetailScreen
import com.bootsnip.aichat.ui.screens.ChatHistoryScreen
import com.bootsnip.aichat.viewmodel.AiViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatHistoryNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: AiViewModel = hiltViewModel(),
    navigateToHome: (Boolean, Int) -> Unit
) {

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: AllDestinations.HOME

    val uid = viewModel.selectedChatHistory.collectAsStateWithLifecycle().value

    val chatHistory = viewModel.chatHistory.collectAsStateWithLifecycle().value.find {
        it.uid == uid
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val chatHistoryTitle = stringResource(id = R.string.chat_history)
                    val title = when (currentRoute) {
                        AllDestinations.CHAT_HISTORY -> chatHistoryTitle
                        AllDestinations.CHAT_HISTORY_DETAIL -> chatHistory?.chatMessageList?.get(0)?.content
                            ?: "Details"

                        else -> chatHistoryTitle
                    }
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = if (title == chatHistoryTitle || title == "Details") FontWeight.Bold else FontWeight.SemiBold,
                        fontSize = if (title == chatHistoryTitle || title == "Details") 24.sp else 16.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentRoute == AllDestinations.CHAT_HISTORY_DETAIL) {
                            navController.popBackStack()
                        } else {
                            navigateToHome(false, 0)
                        }
                    }, content = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    })
                },
                actions = {
                    if (currentRoute == AllDestinations.CHAT_HISTORY_DETAIL) {
                        IconButton(onClick = {
                            viewModel.startNewChat()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = "Localized description",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                )
            )
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = AllDestinations.CHAT_HISTORY,
            modifier = modifier.padding(paddingValues)
        ) {

            composable(
                route = AllDestinations.CHAT_HISTORY,
                enterTransition = {
                    when (initialState.destination.route) {
                        AllDestinations.CHAT_HISTORY_DETAIL ->
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            )

                        else -> null
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        AllDestinations.CHAT_HISTORY_DETAIL ->
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            )

                        else -> null
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        AllDestinations.CHAT_HISTORY_DETAIL ->
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            )

                        else -> null
                    }
                },
                popExitTransition = {
                    when (targetState.destination.route) {
                        AllDestinations.CHAT_HISTORY_DETAIL ->
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            )

                        else -> null
                    }
                }
            ) {
                ChatHistoryScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }

            composable(
                AllDestinations.CHAT_HISTORY_DETAIL,
                enterTransition = {
                    when (initialState.destination.route) {
                        AllDestinations.CHAT_HISTORY ->
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            )

                        else -> null
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        AllDestinations.CHAT_HISTORY ->
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(300)
                            )

                        else -> null
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        AllDestinations.CHAT_HISTORY ->
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            )

                        else -> null
                    }
                },
                popExitTransition = {
                    when (targetState.destination.route) {
                        AllDestinations.CHAT_HISTORY ->
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(300)
                            )

                        else -> null
                    }
                }
            ) {
                ChatHistoryDetailScreen(
                    viewModel = viewModel,
                    navigateHome = {
                        navigateToHome(true, it)
                    }
                )
            }
        }
    }
}