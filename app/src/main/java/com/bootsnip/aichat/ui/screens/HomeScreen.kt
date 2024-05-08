package com.bootsnip.aichat.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aallam.openai.api.core.Role
import com.bootsnip.aichat.R
import com.bootsnip.aichat.ui.components.AiChatBox
import com.bootsnip.aichat.ui.components.AstraSuggestionsDialog
import com.bootsnip.aichat.ui.components.DotsLoadingIndicator
import com.bootsnip.aichat.ui.components.HomePlaceholder
import com.bootsnip.aichat.ui.components.UserChatBox
import com.bootsnip.aichat.viewmodel.AiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AiViewModel
) {
    val gptChatList = viewModel.chatList.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val listState = rememberLazyListState()
    val interactionSource = remember { MutableInteractionSource() }
    var showSuggestionDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(gptChatList.size) {
        if (gptChatList.size > 0) {
            listState.animateScrollToItem(gptChatList.size - 1)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getAllChatHistory()
    }

    if(showSuggestionDialog){
        AstraSuggestionsDialog(
            showSuggestionDialog = {
                showSuggestionDialog = it
            },
            onSuggestionClicked = {
                viewModel.getGPTResponse(it)
            }
        )
    }

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (chatArea, inputField, placeholder) = createRefs()
        var prompt by remember { mutableStateOf("") }

        if (gptChatList.isEmpty()) {
            HomePlaceholder(poweredBy = "Chat GPT", modifier = Modifier.constrainAs(placeholder) {
                top.linkTo(parent.top)
                bottom.linkTo(inputField.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
                showSuggestionDialog = true
            }
        } else {
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

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        contentAlignment = Alignment.Center
                    )
                    {
                        if (isLoading) {
                            DotsLoadingIndicator()
                        }
                    }
                }

            }

        }

        ConstraintLayout(
            modifier = Modifier
                .constrainAs(inputField) {
                    linkTo(chatArea.bottom, parent.bottom, bottomMargin = 16.dp, bias = 1F)
                }
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            val (textBox, sendButton) = createRefs()
            val localStyle = LocalTextStyle.current
            val mergedStyle = localStyle.merge(TextStyle(color = LocalContentColor.current))

            BasicTextField(
                textStyle = mergedStyle,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                maxLines = 6,
                value = prompt,
                onValueChange = { prompt = it },
                modifier = Modifier
                    .defaultMinSize(minHeight = 45.dp)
                    .constrainAs(textBox) {
                        start.linkTo(parent.start)
                        end.linkTo(sendButton.start)
                        width = Dimension.fillToConstraints
                    },
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                enabled = true,
                singleLine = false
            ) { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = prompt,
                    visualTransformation = VisualTransformation.None,
                    innerTextField = innerTextField,
                    singleLine = false,
                    enabled = true,
                    shape = RoundedCornerShape(20.dp),
                    placeholder = { Text("Type here...") },
                    interactionSource = interactionSource,
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp,
                        end = 16.dp
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                )
            }

            ElevatedButton(
                onClick = {
                    viewModel.getGPTResponse(prompt)
                    prompt = ""
                },
                shape = CircleShape,
                modifier = Modifier
                    .constrainAs(sendButton) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(start = 8.dp)
                    .size(45.dp),
                colors = ButtonColors(
                    contentColor = Color.Gray,
                    containerColor = Color.LightGray,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.Gray
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.send_variant),
                    contentDescription = "send_button"
                )
            }
        }
    }
}