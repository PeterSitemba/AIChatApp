package com.bootsnip.aichat.ui.screens

import android.Manifest
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bootsnip.aichat.navigation.Api32AndBelowPermissionRequester
import com.bootsnip.aichat.navigation.Api33PermissionRequester
import com.bootsnip.aichat.navigation.AppNavigationActions
import com.bootsnip.aichat.viewmodel.AstraViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ChatHistoryScreen(
    viewModel: AstraViewModel,
    navigationActions: AppNavigationActions
) {

    LaunchedEffect(Unit) {
        viewModel.getAllChatHistory()
        viewModel.getFavChatHistory()
        viewModel.fetchChatHistoryWithAuthSession()
    }

    val chatHistoryList = viewModel.chatHistory.collectAsStateWithLifecycle().value
    val favChatHistoryList = viewModel.favChatHistory.collectAsStateWithLifecycle().value
    val pagerState = rememberPagerState(
        pageCount = { 2 }
    )
    val coroutineScope = rememberCoroutineScope()

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


    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!readMediaImagesPermission.status.isGranted) {
                if (readMediaImagesPermission.status.shouldShowRationale) {
                    showRationalDialog.value = true
                } else {
                    readMediaImagesPermission.launchPermissionRequest()
                }
            }
        } else {
            if (!multiplePermission.allPermissionsGranted) {
                if (multiplePermission.shouldShowRationale) {
                    showRationalDialog.value = true
                } else {
                    multiplePermission.launchMultiplePermissionRequest()
                }
            }
        }
    }

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

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (tabs, pager) = createRefs()

        TabRow(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(tabs) {
                    top.linkTo(parent.top)
                },
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .fillMaxWidth()
                )
            }
        ) {

            for (i in 0 until pagerState.pageCount) {
                val title = if (i == 0) "All" else "Favorites"
                Tab(
                    modifier = Modifier.fillMaxWidth(),
                    text = { Text(title) },
                    selected = pagerState.currentPage == i,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(i)
                        }
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .constrainAs(pager) {
                    linkTo(tabs.bottom, parent.bottom, bias = 0F)
                    height = Dimension.fillToConstraints
                }
        ) { page ->
            if (page == 0) {
                AllChatHistoryScreen(chatHistoryList = chatHistoryList, navigationActions, viewModel)
            } else {
               FavChatHistoryScreen(chatHistoryList = favChatHistoryList, navigationActions, viewModel)
            }
        }
    }
}