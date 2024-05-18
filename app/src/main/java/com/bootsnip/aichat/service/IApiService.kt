package com.bootsnip.aichat.service

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage

interface IApiService {
    suspend fun getGPTResponse(gptQuery: List<ChatMessage>, modelId: String): ChatCompletionRequest
}