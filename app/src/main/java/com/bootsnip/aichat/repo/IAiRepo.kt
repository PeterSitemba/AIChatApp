package com.bootsnip.aichat.repo

import com.aallam.openai.api.chat.ChatCompletion

interface IAiRepo {
    suspend fun gtpChatResponse(query: String) : ChatCompletion
}