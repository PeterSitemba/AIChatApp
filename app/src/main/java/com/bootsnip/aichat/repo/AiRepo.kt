package com.bootsnip.aichat.repo

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.client.OpenAI
import com.bootsnip.aichat.db.ChatHistory
import com.bootsnip.aichat.db.ChatHistoryDao
import com.bootsnip.aichat.service.IApiService
import com.bootsnip.aichat.util.Key.KEY
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AiRepo @Inject constructor(
    private val service: IApiService,
    private val ioDispatcher: CoroutineDispatcher,
    private val chatHistoryDao: ChatHistoryDao
) : IAiRepo {

    //assistant API calls
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


    //region room db functions

    override fun getAllChatHistory() = chatHistoryDao.getAllChatHistoryDistinct()
    override fun getAllFavChatHistory() = chatHistoryDao.getAllFavChatHistoryDistinct()

    override suspend fun insertChatHistory(chatHistory: ChatHistory) {
        withContext(ioDispatcher){
            chatHistoryDao.insertChatHistory(chatHistory)
        }
    }

    override suspend fun deleteChatHistory(id: Int) {
        withContext(ioDispatcher){
            chatHistoryDao.deleteChatHistory(id)
        }
    }

}