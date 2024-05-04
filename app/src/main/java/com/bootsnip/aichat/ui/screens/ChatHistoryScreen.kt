package com.bootsnip.aichat.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bootsnip.aichat.ui.components.ChatHistoryListItem
import com.bootsnip.aichat.viewmodel.AiViewModel

@Composable
fun ChatHistoryScreen(
    viewModel: AiViewModel
) {

    LaunchedEffect(Unit) {
        viewModel.getAllChatHistory()
    }

    val chatHistoryList = viewModel.chatHistory.collectAsStateWithLifecycle().value

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(chatHistoryList) {
            ChatHistoryListItem(placeHolder = it.chatMessageList?.get(0)?.content ?: "Message")
        }
    }
}