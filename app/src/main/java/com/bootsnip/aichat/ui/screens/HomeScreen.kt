package com.bootsnip.aichat.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
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
import com.bootsnip.aichat.ui.components.AstraTextSelectionActionSheet
import com.bootsnip.aichat.ui.components.DotsLoadingIndicator
import com.bootsnip.aichat.ui.components.HomePlaceholder
import com.bootsnip.aichat.ui.components.NetworkConnectionDialog
import com.bootsnip.aichat.ui.components.UserChatBox
import com.bootsnip.aichat.ui.components.UserTextSelectionActionSheet
import com.bootsnip.aichat.util.FormatChatShare
import com.bootsnip.aichat.util.NetworkConnection
import com.bootsnip.aichat.viewmodel.AstraViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: AstraViewModel,
    onTextSelectClicked: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val gptChatList = viewModel.chatList.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isGPTResponseLoading.collectAsStateWithLifecycle().value
    val tokenError = viewModel.tokensError.collectAsStateWithLifecycle().value
    val errorList = viewModel.errorChatList.collectAsStateWithLifecycle().value

    val listState = rememberLazyListState()
    val interactionSource = remember { MutableInteractionSource() }
    var showSuggestionDialog by remember {
        mutableStateOf(false)
    }
    var showNoInternetDialog by remember {
        mutableStateOf(false)
    }

    var showAstraModalBottomSheet by remember {
        mutableStateOf(false)
    }

    var showUserModalBottomSheet by remember {
        mutableStateOf(false)
    }

    var selectedResponse by remember {
        mutableStateOf("")
    }

    var selectedQuery by remember {
        mutableStateOf("")
    }

    var chatItemIndex by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(gptChatList.size, errorList.size) {
        if (errorList.size > 0) {
            listState.animateScrollToItem(errorList.size - 1)
            return@LaunchedEffect
        }

        if (gptChatList.size > 0) {
            listState.animateScrollToItem(gptChatList.size - 1)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getAllChatHistory()
    }

    if (showSuggestionDialog) {
        AstraSuggestionsDialog(
            showSuggestionDialog = {
                showSuggestionDialog = it
            },
            onSuggestionClicked = {
                viewModel.getGPTResponse(it)
            }
        )
    }

    if (showNoInternetDialog) {
        NetworkConnectionDialog(
            showNoInternetDialog = {
                showNoInternetDialog = it
            }
        )
    }

    if (showAstraModalBottomSheet) {
        AstraTextSelectionActionSheet(
            showRegenerate = chatItemIndex == gptChatList.size - 1,
            showShareEntireChat = chatItemIndex == gptChatList.size - 1,
            onDismissSheet = { showAstraModalBottomSheet = false },
            onCopyClicked = {
                clipboardManager.setText(AnnotatedString((selectedResponse)))
            },
            onSelectTextClicked = {
                viewModel.setSelectedResponse(selectedResponse)
                onTextSelectClicked()
            },
            onRegenerateResponseClicked = {
                viewModel.getGPTResponse(gptChatList[chatItemIndex - 1].content ?: "")
            },
            onShareEntireChatClicked = {
                clipboardManager.setText(AnnotatedString((FormatChatShare(gptChatList).chatToShare())))
            }
        )
    }

    if (showUserModalBottomSheet) {
        UserTextSelectionActionSheet(
            onDismissSheet = { showUserModalBottomSheet = false },
            onCopyClicked = {
                clipboardManager.setText(AnnotatedString((selectedQuery)))
            },
            onSelectTextClicked = {
                viewModel.setSelectedQuery(selectedQuery)
                onTextSelectClicked()
            }
        )
    }

    val haptics = LocalHapticFeedback.current

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (chatArea, inputField, placeholder) = createRefs()
        var prompt by remember { mutableStateOf("") }

        if (gptChatList.isEmpty() && errorList.isEmpty()) {
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
                itemsIndexed(if (tokenError) errorList else gptChatList) { index, item ->
                    Spacer(modifier = Modifier.height(10.dp))
                    when (item.role) {
                        Role("user") -> {
                            UserChatBox(
                                item.content.orEmpty(),
                                modifier = Modifier
                                    .combinedClickable(
                                        onClick = { },
                                        onLongClick = {
                                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                            showUserModalBottomSheet = true
                                            selectedQuery = item.content.orEmpty()
                                        },
                                        onLongClickLabel = stringResource(R.string.new_chat)
                                    )
                            )
                        }

                        Role("assistant") -> {
                            AiChatBox(
                                item.content.orEmpty(),
                                modifier = Modifier
                                    .combinedClickable(
                                        onClick = { },
                                        onLongClick = {
                                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                            showAstraModalBottomSheet = true
                                            selectedResponse = item.content.orEmpty()
                                            chatItemIndex = index
                                        },
                                        onLongClickLabel = stringResource(R.string.new_chat)
                                    )
                            )
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
                    if (NetworkConnection(context).isNetworkAvailable()) {
                        if (prompt.isNotEmpty()) {
                            viewModel.getGPTResponse(prompt)
                        }
                        prompt = ""
                    } else {
                        showNoInternetDialog = true
                    }
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