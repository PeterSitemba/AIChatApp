package com.bootsnip.aichat.navigation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bootsnip.aichat.R
import com.bootsnip.aichat.ui.activities.ChatHistoryActivity
import com.bootsnip.aichat.ui.components.AppDrawer
import com.bootsnip.aichat.ui.components.AstraSnackBar
import com.bootsnip.aichat.ui.components.GPTDropDownList
import com.bootsnip.aichat.ui.components.NewChatDialog
import com.bootsnip.aichat.ui.components.ProgressDialog
import com.bootsnip.aichat.ui.components.SignOutDialog
import com.bootsnip.aichat.ui.screens.AuthenticationScreen
import com.bootsnip.aichat.ui.screens.HomeScreen
import com.bootsnip.aichat.ui.screens.PurchaseSubscriptionScreen
import com.bootsnip.aichat.ui.screens.TextSelectionScreen
import com.bootsnip.aichat.ui.theme.DarkGrey
import com.bootsnip.aichat.viewmodel.AstraViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val CLOSE_DRAWER = "close_drawer"
const val UID = "uid"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AstraNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    viewModel: AstraViewModel = hiltViewModel()
) {

    val isSignedIn = viewModel.isSignedIn.collectAsStateWithLifecycle().value
    val userName = viewModel.userName.collectAsStateWithLifecycle().value
    val gptList = viewModel.gptLLMs.collectAsStateWithLifecycle().value
    val selectedGPTLLM = viewModel.selectedGPTLLM.collectAsStateWithLifecycle().value
    val tokenCount = viewModel.tokensCount.collectAsStateWithLifecycle().value
    val tokenLocal = viewModel.tokensLocal.collectAsStateWithLifecycle().value
    val showPurchaseScreen = viewModel.showPurchaseScreen.collectAsStateWithLifecycle().value
    val showSignInSuccess = viewModel.showSignInSuccessSnackBar.collectAsStateWithLifecycle().value
    val showSignOutSuccess =
        viewModel.showSignOutSuccessSnackBar.collectAsStateWithLifecycle().value
    val showSignOutFailed = viewModel.showSignOutFailedSnackBar.collectAsStateWithLifecycle().value
    val isSnackBarSuccess = viewModel.isSnackBarSuccess.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val chatList = viewModel.chatList.collectAsStateWithLifecycle().value
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: AllDestinations.HOME
    val navigationActions = remember(navController) {
        AppNavigationActions(navController)
    }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    var showDropDown by remember {
        mutableStateOf(false)
    }

    var showNewChatDialog by remember {
        mutableStateOf(false)
    }

    var showSignOutDialog by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val activityResultLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val closeDrawer = it.data?.getBooleanExtra(CLOSE_DRAWER, false)
            val uid = it.data?.getIntExtra(UID, 0)
            if ((closeDrawer != null) && closeDrawer && uid != null) {
                coroutineScope.launch { drawerState.close() }
                viewModel.continueChat(uid)
            }
        }

    val buttonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = DarkGrey,
        contentColor = Color.White,
        disabledContainerColor = Color.Unspecified,
        disabledContentColor = Color.Unspecified
    )

    LaunchedEffect(showPurchaseScreen) {
        if (showPurchaseScreen) {
            navigationActions.navigateToSubscription()
            viewModel.showPurchaseScreen.value = false
        }
    }

    if (showNewChatDialog) {
        NewChatDialog(
            showNewChatDialog = { showNewChatDialog = it },
            startNewChat = { viewModel.startNewChat() }
        )
    }

    if (showSignOutDialog) {
        SignOutDialog(
            showSignOutDialog = { showSignOutDialog = it },
            signOutClicked = { viewModel.signOut() }
        )
    }

    LaunchedEffect(showSignInSuccess) {
        if (showSignInSuccess) {
            scope.launch {
                snackBarHostState.showSnackbar(context.getString(R.string.success_login))
                viewModel.showSignInSuccessSnackBar(false)
            }
        }
    }

    LaunchedEffect(showSignOutSuccess) {
        if (showSignOutSuccess) {
            scope.launch {
                snackBarHostState.showSnackbar(context.getString(R.string.success_logout))
                viewModel.showSignOutSuccessSnackBar(false)
            }
        }
    }

    LaunchedEffect(showSignOutFailed) {
        if (showSignOutFailed) {
            scope.launch {
                snackBarHostState.showSnackbar(context.getString(R.string.failed_logout))
                viewModel.showSignOutFailedSnackBar(false)
            }
        }
    }

    if (isLoading) {
        ProgressDialog()
    }


    val showRationalDialog = remember { mutableStateOf(false) }

    val readMediaImagesPermission = rememberPermissionState(
        permission = Manifest.permission.READ_MEDIA_IMAGES
    )

    val multiplePermission = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    )


    if (showRationalDialog.value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Api33PermissionRequester(
                rationalDialog = showRationalDialog.value,
                showRationalDialog = {
                    showRationalDialog.value = it
                }
            )
        } else {
            Api32AndBelowPermissionRequester(
                multiplePermission = multiplePermission,
                rationalDialog = showRationalDialog.value,
                showRationalDialog = {
                    showRationalDialog.value = it
                }
            )
        }
    }


    ModalNavigationDrawer(
        drawerContent = {
            if (currentRoute != AllDestinations.AUTHENTICATION) {
                val isUnlimited =
                    tokenLocal.firstOrNull()?.unlimited ?: false

                AppDrawer(
                    route = currentRoute,
                    unlimited = isUnlimited,
                    isSignedIn = isSignedIn,
                    userName = userName.orEmpty(),
                    navigateToHome = { navigationActions.navigateToHome() },
                    navigateToChatHistory = {
                        activityResultLauncher.launch(
                            Intent(
                                context,
                                ChatHistoryActivity::class.java
                            )
                        )
                    },
                    navigateToAuthentication = {
                        navigationActions.navigateToAuthentication()
                    },
                    navigateToSubscription = {
                        navigationActions.navigateToSubscription()
                    },
                    signOutClicked = {
                        showSignOutDialog = true
                    },
                    closeDrawer = { coroutineScope.launch { drawerState.close() } },
                    modifier = Modifier
                )
            }
        }, drawerState = drawerState,
        gesturesEnabled = currentRoute == AllDestinations.HOME

    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        when (currentRoute) {
                            AllDestinations.HOME -> {
                                ExposedDropdownMenuBox(
                                    expanded = showDropDown,
                                    onExpandedChange = { showDropDown = !showDropDown }
                                ) {

                                    Button(
                                        onClick = {},
                                        contentPadding = PaddingValues(
                                            bottom = 8.dp,
                                            top = 8.dp,
                                            start = 12.dp,
                                            end = 4.dp
                                        ),
                                        colors = buttonColors,
                                        modifier = Modifier
                                            .width(170.dp)
                                            .menuAnchor()
                                    ) {
                                        Text(text = selectedGPTLLM?.displayName ?: "GPT - 3.5")

                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = showDropDown
                                        )
                                    }

                                    GPTDropDownList(
                                        llmList = gptList,
                                        expanded = showDropDown,
                                        unlimited = tokenLocal.firstOrNull()?.unlimited
                                            ?: false,
                                        showDropDown = { showDropDown = it },
                                        selectedLLM = { selectedLLM, subscribed ->
                                            val isUnlimited =
                                                tokenLocal.firstOrNull()?.unlimited ?: false

                                            val selectLLM = gptList.find { it.llmVersion == selectedLLM }

                                            val isImageLLM = selectLLM?.llmVersion?.startsWith("dall-e") ?: false

                                            if(isImageLLM) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                    if (!readMediaImagesPermission.status.isGranted) {
                                                        if (readMediaImagesPermission.status.shouldShowRationale) {
                                                            showRationalDialog.value = true
                                                        } else {
                                                            readMediaImagesPermission.launchPermissionRequest()
                                                        }
                                                    } else {
                                                        setSelectedLLM(
                                                            viewModel = viewModel,
                                                            isUnlimited = isUnlimited,
                                                            selectedLLM = selectedLLM,
                                                            subscribed = subscribed
                                                        )
                                                    }
                                                } else {
                                                    if (!multiplePermission.allPermissionsGranted) {
                                                        if (multiplePermission.shouldShowRationale) {
                                                            showRationalDialog.value = true
                                                        } else {
                                                            multiplePermission.launchMultiplePermissionRequest()
                                                        }
                                                    } else {
                                                        setSelectedLLM(
                                                            viewModel = viewModel,
                                                            isUnlimited = isUnlimited,
                                                            selectedLLM = selectedLLM,
                                                            subscribed = subscribed
                                                        )
                                                    }
                                                }

                                            } else {
                                                setSelectedLLM(
                                                    viewModel = viewModel,
                                                    isUnlimited = isUnlimited,
                                                    selectedLLM = selectedLLM,
                                                    subscribed = subscribed
                                                )
                                            }
                                        },
                                        modifier = Modifier.width(170.dp)
                                    )
                                }

                            }
                        }

                    },
                    modifier = Modifier.fillMaxWidth(),
                    navigationIcon = {
                        when (currentRoute) {
                            AllDestinations.HOME -> {
                                IconButton(onClick = {
                                    coroutineScope.launch { drawerState.open() }
                                }, content = {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = null
                                    )
                                })

                            }

                            AllDestinations.AUTHENTICATION, AllDestinations.SUBSCRIPTION, AllDestinations.TEXT_SELECTION -> {
                                IconButton(onClick = {
                                    navController.popBackStack()
                                }, content = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null
                                    )
                                })
                            }
                        }

                    }, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                    ),
                    actions = {
                        when (currentRoute) {
                            AllDestinations.HOME -> {
                                val isUnlimited =
                                    tokenLocal.firstOrNull()?.unlimited ?: false
                                if (!isUnlimited) {
                                    OutlinedButton(
                                        contentPadding = PaddingValues(
                                            start = 4.dp,
                                            top = 0.dp,
                                            end = 4.dp,
                                            bottom = 0.dp,
                                        ),
                                        modifier = Modifier
                                            .width(60.dp)
                                            .height(30.dp),
                                        onClick = {
                                            navigationActions.navigateToSubscription()
                                        }
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = tokenCount.toString())
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Icon(
                                                painter = painterResource(id = R.drawable.creation),
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .size(16.dp)
                                            )
                                        }
                                    }

                                }

                                IconButton(onClick = {
                                    if (chatList.isNotEmpty()) {
                                        showNewChatDialog = true
                                    }
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.circle_edit_outline),
                                        contentDescription = "Localized description",
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }

                            else -> {}
                        }

                    }
                )
            },
            modifier = Modifier,
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState) { snackbarData ->
                    AstraSnackBar(
                        message = snackbarData.visuals.message,
                        success = isSnackBarSuccess
                    )
                }
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = AllDestinations.HOME,
                modifier = modifier.padding(it)
            ) {

                composable(
                    route = AllDestinations.HOME,
                    enterTransition = {
                        when (initialState.destination.route) {
                            AllDestinations.AUTHENTICATION -> {
                                fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                                        scaleIn(
                                            initialScale = 0.92f,
                                            animationSpec = tween(220, delayMillis = 90)
                                        )
                            }


                            else -> null
                        }
                    },
                    popEnterTransition = {
                        when (initialState.destination.route) {
                            AllDestinations.AUTHENTICATION -> {
                                fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                                        scaleIn(
                                            initialScale = 0.92f,
                                            animationSpec = tween(220, delayMillis = 90)
                                        )
                            }

                            else -> null
                        }
                    },
                    exitTransition = {
                        when (targetState.destination.route) {
                            AllDestinations.AUTHENTICATION -> {
                                scaleOut(targetScale = 0.92f, animationSpec = tween(90)) +
                                        fadeOut(animationSpec = tween(90))
                            }

                            AllDestinations.TEXT_SELECTION ->
                                fadeOut(
                                    animationSpec = tween(300)
                                )

                            else -> null
                        }
                    }
                ) {
                    HomeScreen(
                        viewModel = viewModel,
                        onTextSelectClicked = {
                            navigationActions.navigateToTextSelection()
                        }
                    )
                }

                composable(
                    route = AllDestinations.AUTHENTICATION,
                    popExitTransition = {
                        when (targetState.destination.route) {
                            AllDestinations.HOME -> {
                                scaleOut(targetScale = 0.92f, animationSpec = tween(90)) +
                                        fadeOut(animationSpec = tween(90))
                            }

                            else -> null
                        }
                    }
                ) {
                    AuthenticationScreen(viewModel, navController)
                }

                composable(
                    route = AllDestinations.SUBSCRIPTION,
                    enterTransition = {
                        when (initialState.destination.route) {
                            AllDestinations.HOME -> {
                                slideInVertically(animationSpec = tween(500)) { fullHeight ->
                                    fullHeight
                                }
                            }

                            else -> null
                        }
                    },
                    exitTransition = {
                        when (targetState.destination.route) {
                            AllDestinations.HOME -> {
                                slideOutVertically(animationSpec = tween(500)) { fullHeight ->
                                    fullHeight
                                }
                            }

                            else -> null
                        }
                    },
                    popEnterTransition = {
                        when (initialState.destination.route) {
                            AllDestinations.HOME -> {
                                slideInVertically(animationSpec = tween(500)) { height ->
                                    height
                                }
                            }

                            else -> null
                        }
                    },
                    popExitTransition = {
                        when (targetState.destination.route) {
                            AllDestinations.HOME -> {
                                slideOutVertically(animationSpec = tween(500)) { fullHeight ->
                                    fullHeight
                                }
                            }

                            else -> null
                        }
                    }

                ) {
                    PurchaseSubscriptionScreen()
                }

                composable(
                    route = AllDestinations.TEXT_SELECTION,
                    enterTransition = {
                        when (initialState.destination.route) {
                            AllDestinations.HOME -> {
                                slideInVertically(animationSpec = tween(500)) { fullHeight ->
                                    fullHeight
                                }
                            }

                            else -> null
                        }
                    },
                    exitTransition = {
                        when (targetState.destination.route) {
                            AllDestinations.HOME -> {
                                slideOutVertically(animationSpec = tween(500)) { fullHeight ->
                                    fullHeight
                                }
                            }

                            else -> null
                        }
                    },
                    popEnterTransition = {
                        when (initialState.destination.route) {
                            AllDestinations.HOME -> {
                                slideInVertically(animationSpec = tween(500)) { height ->
                                    height
                                }
                            }

                            else -> null
                        }
                    },
                    popExitTransition = {
                        when (targetState.destination.route) {
                            AllDestinations.HOME -> {
                                slideOutVertically(animationSpec = tween(500)) { fullHeight ->
                                    fullHeight
                                }
                            }

                            else -> null
                        }
                    }

                ) {
                    TextSelectionScreen(viewModel)
                }

            }
        }
    }
}

private fun setSelectedLLM(
    viewModel: AstraViewModel,
    isUnlimited: Boolean,
    selectedLLM: String,
    subscribed: Boolean
) {
    if (isUnlimited) {
        viewModel.setSelectedLLM(selectedLLM)
    } else {
        viewModel.showPurchaseScreen.value = subscribed
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Api33PermissionRequester(
    rationalDialog: Boolean,
    showRationalDialog: (Boolean) -> Unit
) {

    val context = LocalContext.current

    if (rationalDialog) {
        AlertDialog(
            onDismissRequest = {
                showRationalDialog(false)
            },
            title = {
                Text(
                    text = "Permission",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    "We need read media permissions to enable image generation. Please grant the permission.",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRationalDialog(false)
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(context, intent, null)

                    }) {
                    Text("OK", style = TextStyle(color = Color.White))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRationalDialog(false)
                    }) {
                    Text("Cancel", style = TextStyle(color = Color.White))
                }
            },
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Api32AndBelowPermissionRequester(
    multiplePermission: MultiplePermissionsState,
    rationalDialog: Boolean,
    showRationalDialog: (Boolean) -> Unit
) {

    val context = LocalContext.current

    if (rationalDialog) {
        AlertDialog(
            onDismissRequest = {
                showRationalDialog(false)
            },
            title = {
                Text(
                    text = "Permission",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    if (multiplePermission.revokedPermissions.size == 2) {
                        "We need read and write permission to generate images. Please grant the permission."
                    } else if (multiplePermission.revokedPermissions.first().permission == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                        "We need write permission to generate images. Please grant the permission."
                    } else {
                        "We need read permission to generate images. Please grant the permission."
                    },
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRationalDialog(false)
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(context, intent, null)

                    }) {
                    Text("OK", style = TextStyle(color = Color.Black))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRationalDialog(false)
                    }) {
                    Text("Cancel", style = TextStyle(color = Color.Black))
                }
            },
        )
    }
}