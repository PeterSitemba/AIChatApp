package com.bootsnip.aichat.repo

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.auth.AuthUser
import com.amplifyframework.datastore.DataStoreItemChange
import com.amplifyframework.datastore.generated.model.ChatHistoryRemote
import com.amplifyframework.datastore.generated.model.ChatMessageObject
import com.amplifyframework.datastore.generated.model.OpenAi
import kotlinx.coroutines.flow.Flow

interface IAiRepo {
    suspend fun gtpChatResponse(query: List<ChatMessage>, openAiAuth: String): ChatCompletion

    //region amplify
    suspend fun fetchAuthSession(): AuthSession

    suspend fun observeDataStore(): Flow<DataStoreItemChange<OpenAi>>

    suspend fun queryDataStore(): Flow<OpenAi>

    suspend fun getCurrentUser(): AuthUser

    suspend fun observeChatHistory(): Flow<DataStoreItemChange<ChatHistoryRemote>>

    suspend fun queryChatHistory(): Flow<ChatHistoryRemote>

    suspend fun saveChatHistory(chatHistoryRemote: ChatHistoryRemote)

    suspend fun updateChatHistory(chatList: List<ChatMessageObject>, id: String)

    suspend fun observeFavChatHistory(): Flow<DataStoreItemChange<ChatHistoryRemote>>

    suspend fun queryFavChatHistory(): Flow<ChatHistoryRemote>
}