package com.bootsnip.aichat.repo

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.auth.AuthUser
import com.amplifyframework.datastore.DataStoreItemChange
import com.amplifyframework.datastore.generated.model.ChatHistoryRemote
import com.amplifyframework.datastore.generated.model.OpenAi
import com.bootsnip.aichat.db.ChatHistory
import com.bootsnip.aichat.db.ChatHistoryUpdate
import com.bootsnip.aichat.db.ChatHistoryUpdateFav
import com.bootsnip.aichat.db.Tokens
import com.bootsnip.aichat.db.TokensUpdate
import kotlinx.coroutines.flow.Flow

interface IAiRepo {
    suspend fun gtpChatResponse(query: List<ChatMessage>, openAiAuth: String): ChatCompletion

    //region room db chat history
    fun insertChatHistory(chatHistory: ChatHistory): Long

    fun getAllChatHistory(): Flow<List<ChatHistory>>

    fun getAllFavChatHistory(): Flow<List<ChatHistory>>

    suspend fun updateChatHistory(chatHistoryUpdate: ChatHistoryUpdate)

    suspend fun updateChatHistoryFavStatus(chatHistoryUpdateFav: ChatHistoryUpdateFav)

    suspend fun deleteChatHistory(id: Int)

    //region room db tokens

    suspend fun insertTokens(tokens: Tokens)

    fun getTokens(): Flow<List<Tokens>>

    suspend fun updateLocalTokens(tokensUpdate: TokensUpdate)

    //region amplify
    suspend fun fetchAuthSession(): AuthSession

    suspend fun observeDataStore(): Flow<DataStoreItemChange<OpenAi>>

    suspend fun queryDataStore(): Flow<OpenAi>

    suspend fun getCurrentUser(): AuthUser

    suspend fun observeChatHistory(userId: String): Flow<DataStoreItemChange<ChatHistoryRemote>>

    suspend fun queryChatHistory(userId: String): Flow<ChatHistoryRemote>

    suspend fun saveChatHistory(chatHistoryRemote: ChatHistoryRemote)

    suspend fun updateChatHistoryRemote(chatHistoryRemote: ChatHistoryRemote)

    suspend fun updateChatHistoryUserId(userId: String, id: String)

    suspend fun observeFavChatHistory(): Flow<DataStoreItemChange<ChatHistoryRemote>>

    suspend fun queryFavChatHistory(): Flow<ChatHistoryRemote>
}