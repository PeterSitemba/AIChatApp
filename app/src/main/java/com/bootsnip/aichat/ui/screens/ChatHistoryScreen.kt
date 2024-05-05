package com.bootsnip.aichat.ui.screens

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bootsnip.aichat.viewmodel.AiViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatHistoryScreen(
    viewModel: AiViewModel = hiltViewModel(),
    modifier: Modifier
) {

    LaunchedEffect(Unit) {
        viewModel.getAllChatHistory()
        viewModel.getFavChatHistory()
    }

    val chatHistoryList = viewModel.chatHistory.collectAsStateWithLifecycle().value
    val favChatHistoryList = viewModel.favChatHistory.collectAsStateWithLifecycle().value
    val pagerState = rememberPagerState(
        pageCount = { 2 }
    )
    val coroutineScope = rememberCoroutineScope()

    ConstraintLayout(modifier = modifier.fillMaxSize()) {

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
                AllChatHistoryScreen(chatHistoryList = chatHistoryList)
            } else {
               FavChatHistoryScreen(chatHistoryList = favChatHistoryList)
            }
        }
    }
}