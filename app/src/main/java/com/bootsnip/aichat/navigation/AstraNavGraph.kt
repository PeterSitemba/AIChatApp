package com.bootsnip.aichat.navigation

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bootsnip.aichat.R
import com.bootsnip.aichat.ui.activities.ChatHistoryActivity
import com.bootsnip.aichat.ui.components.AppDrawer
import com.bootsnip.aichat.ui.screens.HomeScreen
import com.bootsnip.aichat.viewmodel.AiViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val CLOSE_DRAWER = "close_drawer"
const val UID = "uid"
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
    val context = LocalContext.current
    val activityResultLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
        val closeDrawer = it.data?.getBooleanExtra(CLOSE_DRAWER, false)
        val uid = it.data?.getIntExtra(UID, 0)
        if((closeDrawer != null) && closeDrawer && uid != null){
            coroutineScope.launch { drawerState.close() }
            viewModel.continueChat(uid)
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                route = currentRoute,
                navigateToHome = { navigationActions.navigateToHome() },
                navigateToChatHistory = {
                    activityResultLauncher.launch(
                        Intent(
                            context,
                            ChatHistoryActivity::class.java
                        )
                    )
                },
                closeDrawer = { coroutineScope.launch { drawerState.close() } },
                modifier = Modifier
            )
        }, drawerState = drawerState,
        gesturesEnabled = currentRoute == AllDestinations.HOME

    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            fontWeight = FontWeight.Bold
                        )
                    },
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
                            viewModel.startNewChat()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = "Localized description",
                                tint = MaterialTheme.colorScheme.secondary
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
            }
        }
    }
}