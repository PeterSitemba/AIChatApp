package com.bootsnip.aichat.repo

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage

interface IAiRepo {
    suspend fun gtpChatResponse(query: List<ChatMessage>) : ChatCompletion
}