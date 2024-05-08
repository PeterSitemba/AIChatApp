package com.bootsnip.aichat.repo

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.client.OpenAI
import com.bootsnip.aichat.db.ChatHistory
import com.bootsnip.aichat.db.ChatHistoryDao
import com.bootsnip.aichat.db.ChatHistoryUpdate
import com.bootsnip.aichat.db.ChatHistoryUpdateFav
import com.bootsnip.aichat.service.IApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AiRepo @Inject constructor(
    private val service: IApiService,
    private val ioDispatcher: CoroutineDispatcher,
    private val chatHistoryDao: ChatHistoryDao
) : IAiRepo {

    override suspend fun gtpChatResponse(query: List<ChatMessage>, openAiAuth: String): ChatCompletion =
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

            OpenAI(openAiAuth).chatCompletion(service.getGPTResponse(completeQuery.toList()))
        }


    //region room db functions

    override fun getAllChatHistory() = chatHistoryDao.getAllChatHistoryDistinct()

    override fun getAllFavChatHistory() = chatHistoryDao.getAllFavChatHistoryDistinct()
    override suspend fun updateChatHistory(chatHistoryUpdate: ChatHistoryUpdate) =
        withContext(ioDispatcher) {
            chatHistoryDao.updateChatHistory(chatHistoryUpdate)
        }


    override suspend fun updateChatHistoryFavStatus(chatHistoryUpdateFav: ChatHistoryUpdateFav) =
        withContext(ioDispatcher) {
            chatHistoryDao.updateChatHistoryFavStatus(chatHistoryUpdateFav)
        }

    override fun insertChatHistory(chatHistory: ChatHistory) =
        chatHistoryDao.insertChatHistory(chatHistory)

    override suspend fun deleteChatHistory(id: Int) =
        withContext(ioDispatcher) {
            chatHistoryDao.deleteChatHistory(id)
        }
}