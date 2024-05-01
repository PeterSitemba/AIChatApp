package com.bootsnip.aichat.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.core.Role
import com.bootsnip.aichat.R
import com.bootsnip.aichat.ui.components.AiChatBox
import com.bootsnip.aichat.ui.components.UserChatBox
import com.bootsnip.aichat.viewmodel.AiViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AiViewModel = hiltViewModel()
) {
    val gptChatList = viewModel.chatList.collectAsStateWithLifecycle().value
    val listState = rememberLazyListState()

    LaunchedEffect(gptChatList.size) {
        if(gptChatList.size > 0){
            listState.animateScrollToItem(gptChatList.size - 1)
        }
    }

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (chatArea, inputField) = createRefs()
        var prompt by remember { mutableStateOf("") }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(chatArea) {
                    top.linkTo(parent.top)
                    bottom.linkTo(inputField.top)
                    height = Dimension.fillToConstraints
                }
                .padding(16.dp)
        ) {
            items(gptChatList) {
                Spacer(modifier = Modifier.height(10.dp))
                when (it.role) {
                    Role("user") -> {
                        UserChatBox(it.content.orEmpty())
                    }

                    Role("assistant") -> {
                        AiChatBox(it.content.orEmpty())
                    }

                    else -> {}
                }
            }
        }

        ConstraintLayout(
            modifier = Modifier
                .constrainAs(inputField) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                }
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            val (textbox, sendButton) = createRefs()

            TextField(
                modifier = Modifier.constrainAs(textbox) {
                    start.linkTo(parent.start)
                    end.linkTo(sendButton.start)
                    width = Dimension.fillToConstraints
                },
                value = prompt,
                onValueChange = { prompt = it },
                placeholder = { Text("Message") },
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            Button(
                onClick = {
                    viewModel.getGPTResponse(prompt)
                    prompt = ""
                },
                shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .constrainAs(sendButton) {
                        end.linkTo(parent.end)
                    },
                colors = ButtonColors(
                    contentColor = Color.Black,
                    containerColor = Color.White,
                    disabledContainerColor = Color.White,
                    disabledContentColor = Color.Gray
                )
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.send),
                    contentDescription = "send_button"
                )
            }
        }


    }
}