package com.bootsnip.aichat.service

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.model.ModelId
import io.ktor.client.HttpClient
import javax.inject.Inject

class ApiService @Inject constructor(
    private val client: HttpClient
): IApiService {
    override suspend fun getGPTResponse(gptQuery: List<ChatMessage>): ChatCompletionRequest =
        ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = gptQuery
        )
}