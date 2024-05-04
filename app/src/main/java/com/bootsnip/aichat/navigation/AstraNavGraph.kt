package com.bootsnip.aichat.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bootsnip.aichat.R
import com.bootsnip.aichat.db.ChatHistory
import com.bootsnip.aichat.model.AstraChatMessage
import com.bootsnip.aichat.ui.components.AppDrawer
import com.bootsnip.aichat.ui.screens.ChatHistoryScreen
import com.bootsnip.aichat.ui.screens.HomeScreen
import com.bootsnip.aichat.util.AssistantType.GPT35TURBO
import com.bootsnip.aichat.viewmodel.AiViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AstraNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    viewModel: AiViewModel = hiltViewModel()
) {

    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: AllDestinations.HOME
    val navigationActions = remember(navController) {
        AppNavigationActions(navController)
    }
    val chatList = viewModel.chatList.collectAsStateWithLifecycle().value.map { chatMessage ->
        AstraChatMessage(
            chatMessage.role,
            chatMessage.content
        )
    }

    ModalNavigationDrawer(drawerContent = {
        AppDrawer(
            route = currentRoute,
            navigateToHome = { navigationActions.navigateToHome() },
            navigateToSettings = { navigationActions.navigateToSettings() },
            closeDrawer = { coroutineScope.launch { drawerState.close() } },
            modifier = Modifier
        )
    }, drawerState = drawerState) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch { drawerState.open() }
                        }, content = {
                            Icon(
                                imageVector = Icons.Default.Menu, contentDescription = null
                            )
                        })
                    }, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    actions = {
                        IconButton(onClick = {
                            val chatHistory = ChatHistory(
                                assistantType = GPT35TURBO.assistantType,
                                chatMessageList = chatList,
                                fav = 0
                            )
                            if (chatList.size > 1) {
                                viewModel.insertChatHistory(chatHistory)
                            }
                            viewModel.chatList.value = mutableListOf()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                )
            }, modifier = Modifier
        ) {
            NavHost(
                navController = navController,
                startDestination = AllDestinations.HOME,
                modifier = modifier.padding(it)
            ) {

                composable(AllDestinations.HOME) {
                    HomeScreen(
                        viewModel
                    )
                }

                composable(AllDestinations.CHAT_HISTORY) {
                    ChatHistoryScreen(
                        viewModel
                    )
                }
            }
        }
    }
}