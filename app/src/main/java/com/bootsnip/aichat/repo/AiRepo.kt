package com.bootsnip.aichat.repo

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.client.OpenAI
import com.bootsnip.aichat.service.IApiService
import com.bootsnip.aichat.util.Key.KEY
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AiRepo @Inject constructor(
    private val service: IApiService,
    private val ioDispatcher: CoroutineDispatcher
) : IAiRepo {

    private val openAI = OpenAI(KEY)

    override suspend fun gtpChatResponse(query: List<ChatMessage>): ChatCompletion =
        withContext(ioDispatcher) {
            openAI.chatCompletion(service.getGPTResponse(query))
        }
}