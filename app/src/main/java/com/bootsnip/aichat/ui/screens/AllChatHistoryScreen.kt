package com.bootsnip.aichat.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bootsnip.aichat.db.ChatHistory
import com.bootsnip.aichat.ui.components.ChatHistoryListItem

@Composable
fun AllChatHistoryScreen(
    chatHistoryList: List<ChatHistory>
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(chatHistoryList, key = { chatHistory -> chatHistory.uid as Any }) {
            Spacer(modifier = Modifier.height(8.dp))

            ChatHistoryListItem(
                placeHolder = it.chatMessageList?.get(0)?.content ?: "Message",
                isFav = it.fav == 1,
                id = it.uid!!
            )

            HorizontalDivider(
                Modifier.padding(top = 8.dp)
            )
        }
    }
}