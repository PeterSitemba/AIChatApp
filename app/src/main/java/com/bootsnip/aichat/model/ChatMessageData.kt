package com.bootsnip.aichat.model

import com.aallam.openai.api.chat.ChatRole

data class ChatMessageData(
    val role: ChatRole,
    val message: String
)
