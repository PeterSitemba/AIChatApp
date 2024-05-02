package com.bootsnip.aichat.repo

import android.util.Log
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
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
            val completeQuery: MutableList<ChatMessage> = mutableListOf()
            completeQuery.add(
                ChatMessage(
                    role = ChatRole.System,
                    name = "Astra",
                    content = "You are a helpful assistant!"
                )
            )
            completeQuery.addAll(query)

            openAI.chatCompletion(service.getGPTResponse(completeQuery.toList()))
        }
}