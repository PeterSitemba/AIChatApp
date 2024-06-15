package com.bootsnip.aichat.repo

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.image.ImageCreation
import com.aallam.openai.api.image.ImageURL
import com.aallam.openai.api.image.ImageVariation
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.auth.AuthUser
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.result.AuthSignOutResult
import com.amplifyframework.datastore.DataStoreItemChange
import com.amplifyframework.datastore.DataStoreQuerySnapshot
import com.amplifyframework.datastore.generated.model.ChatGPTLLMs
import com.amplifyframework.datastore.generated.model.ChatHistoryRemote
import com.amplifyframework.datastore.generated.model.OpenAi
import com.amplifyframework.datastore.generated.model.Suggestions
import com.amplifyframework.datastore.generated.model.TestProUserManagement
import com.amplifyframework.datastore.generated.model.TokenManagement
import com.amplifyframework.hub.HubEvent
import com.bootsnip.aichat.db.ChatHistory
import com.bootsnip.aichat.db.ChatHistoryUpdate
import com.bootsnip.aichat.db.ChatHistoryUpdateFav
import com.bootsnip.aichat.db.Tokens
import com.bootsnip.aichat.db.TokensUpdate
import kotlinx.coroutines.flow.Flow

interface IAstraRepo {

    //region openAI
    suspend fun gtpChatResponse(query: List<ChatMessage>, openAiAuth: String, modelId: String): ChatCompletion

    suspend fun gptImageCreation(imageCreation: ImageCreation, openAiAuth: String): List<ImageURL>

    suspend fun gptImageVariation(imageVariation: ImageVariation, openAiAuth: String): List<ImageURL>

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

    suspend fun observeDataStore(): Flow<DataStoreQuerySnapshot<OpenAi>>

    suspend fun queryDataStore(): Flow<OpenAi>

    suspend fun observeGPTLLMs(): Flow<DataStoreQuerySnapshot<ChatGPTLLMs>>

    suspend fun queryGPTLLMs(): Flow<ChatGPTLLMs>

    suspend fun getCurrentUser(): AuthUser

    suspend fun fetchUserAttributes(): List<AuthUserAttribute>

    suspend fun signOut(): AuthSignOutResult

    suspend fun observeChatHistory(userId: String): Flow<DataStoreItemChange<ChatHistoryRemote>>

    suspend fun queryChatHistory(userId: String): Flow<ChatHistoryRemote>

    suspend fun saveChatHistory(chatHistoryRemote: ChatHistoryRemote)

    suspend fun updateChatHistoryRemote(chatHistoryRemote: ChatHistoryRemote)

    suspend fun updateChatHistoryUserId(userId: String, id: String)

    suspend fun observeFavChatHistory(): Flow<DataStoreItemChange<ChatHistoryRemote>>

    suspend fun queryFavChatHistory(): Flow<ChatHistoryRemote>

    suspend fun dataStoreEventHub(): Flow<HubEvent<*>>

    suspend fun saveTokenManagement(tokenManagement: TokenManagement)

    suspend fun updateTokenManagement(tokenManagement: TokenManagement)

    suspend fun observeTokenManagement(userId: String, identityId: String): Flow<DataStoreQuerySnapshot<TokenManagement>>

    suspend fun queryTokenManagement(userId: String, identityId: String): Flow<TokenManagement>

    suspend fun observeTokenManagementChanged(userId: String, identityId: String): Flow<DataStoreItemChange<TokenManagement>>

    suspend fun querySuggestions(): Flow<Suggestions>

    suspend fun observeSuggestions(): Flow<DataStoreItemChange<Suggestions>>

    suspend fun queryProUserTestManagement(userId: String): Flow<TestProUserManagement>

    suspend fun observeProUserTestManagement(userId: String): Flow<DataStoreItemChange<TestProUserManagement>>

}