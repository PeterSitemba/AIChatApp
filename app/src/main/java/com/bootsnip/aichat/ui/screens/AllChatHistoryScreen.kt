package com.bootsnip.aichat.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bootsnip.aichat.R
import com.bootsnip.aichat.db.ChatHistory
import com.bootsnip.aichat.navigation.AppNavigationActions
import com.bootsnip.aichat.ui.components.ChatHistoryListItem
import com.bootsnip.aichat.ui.components.EmptyScreenPlaceholder
import com.bootsnip.aichat.viewmodel.AiViewModel

@Composable
fun AllChatHistoryScreen(
    chatHistoryList: List<ChatHistory>,
    navHostController: NavHostController,
    viewModel: AiViewModel
) {

    val navigationActions = remember(navHostController) {
        AppNavigationActions(navHostController)
    }

    if (chatHistoryList.isEmpty()) {
        EmptyScreenPlaceholder(textPlaceHolder = stringResource(id = R.string.empty_chat_history))
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(chatHistoryList, key = { chatHistory -> chatHistory.uid as Any }) {
                Spacer(modifier = Modifier.height(8.dp))

                ChatHistoryListItem(
                    modifier = Modifier.clickable{
                        viewModel.selectedChatHistory.value = it.uid!!
                        navigationActions.navigateToChatHistoryDetail()
                    },
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
}