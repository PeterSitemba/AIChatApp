package com.bootsnip.aichat.repo

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.datastore.DataStoreItemChange
import com.amplifyframework.datastore.generated.model.OpenAi
import com.bootsnip.aichat.db.ChatHistory
import com.bootsnip.aichat.db.ChatHistoryUpdate
import com.bootsnip.aichat.db.ChatHistoryUpdateFav
import kotlinx.coroutines.flow.Flow

interface IAiRepo {
    suspend fun gtpChatResponse(query: List<ChatMessage>, openAiAuth: String): ChatCompletion

    fun insertChatHistory(chatHistory: ChatHistory): Long

    fun getAllChatHistory(): Flow<List<ChatHistory>>

    fun getAllFavChatHistory(): Flow<List<ChatHistory>>

    suspend fun updateChatHistory(chatHistoryUpdate: ChatHistoryUpdate)

    suspend fun updateChatHistoryFavStatus(chatHistoryUpdateFav: ChatHistoryUpdateFav)

    suspend fun deleteChatHistory(id: Int)

    //region amplify
    suspend fun fetchAuthSession(): AuthSession

    suspend fun observeDataStore(): Flow<DataStoreItemChange<OpenAi>>

    suspend fun queryDataStore(): Flow<OpenAi>
}