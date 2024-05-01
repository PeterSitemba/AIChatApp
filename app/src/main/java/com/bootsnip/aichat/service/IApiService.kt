package com.bootsnip.aichat.service

import com.aallam.openai.api.chat.ChatCompletionRequest

interface IApiService {
    suspend fun getGPTResponse(gptQuery: String): ChatCompletionRequest
}