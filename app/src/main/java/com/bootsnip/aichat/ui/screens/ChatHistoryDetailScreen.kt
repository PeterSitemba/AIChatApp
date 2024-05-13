package com.bootsnip.aichat.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bootsnip.aichat.ui.components.AiChatBox
import com.bootsnip.aichat.ui.components.UserChatBox
import com.bootsnip.aichat.ui.theme.Purple40
import com.bootsnip.aichat.viewmodel.AstraViewModel

@Composable
fun ChatHistoryDetailScreen(
    viewModel: AstraViewModel,
    navigateHome: (String) -> Unit
) {
    val listState = rememberLazyListState()
    val uid = viewModel.selectedChatHistory.collectAsStateWithLifecycle().value

    val chatHistory = viewModel.chatHistoryRemote.collectAsStateWithLifecycle().value.find {
        it.id == uid
    }

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (chatArea, inputField) = createRefs()

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(chatArea) {
                    linkTo(parent.top, inputField.top, bias = 0F)
                    height = Dimension.fillToConstraints
                }
        ) {
            if (chatHistory != null) {
                items(chatHistory.chatMessageList!!) {
                    Spacer(modifier = Modifier.height(10.dp))
                    when (it.role) {
                        "user" -> {
                            UserChatBox(it.content.orEmpty())
                        }

                        "assistant" -> {
                            AiChatBox(it.content.orEmpty())
                        }

                        else -> {}
                    }
                }

            }
        }

        Box(
            modifier = Modifier
                .constrainAs(inputField) {
                    linkTo(chatArea.bottom, parent.bottom, bias = 1F)
                }
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            ElevatedButton(
                onClick = {
                    uid?.let{
                        navigateHome(it)
                    }
                },
                modifier = Modifier.align(Alignment.Center),
                colors = ButtonColors(
                    containerColor = Purple40,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Unspecified,
                    disabledContentColor = Color.Unspecified
                )
            ) {
                Text(
                    text = "Continue Chat", Modifier.padding(5.dp),
                    fontSize = 16.sp
                )
            }

        }
    }


}