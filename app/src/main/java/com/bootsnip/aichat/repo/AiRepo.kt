package com.bootsnip.aichat.repo

import android.util.Log
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.client.OpenAI
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.auth.AuthUser
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.auth.result.AuthSignOutResult
import com.amplifyframework.core.model.query.ObserveQueryOptions
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.DataStoreChannelEventName
import com.amplifyframework.datastore.DataStoreException
import com.amplifyframework.datastore.DataStoreItemChange
import com.amplifyframework.datastore.DataStoreQuerySnapshot
import com.amplifyframework.datastore.generated.model.ChatGPTLLMs
import com.amplifyframework.datastore.generated.model.ChatHistoryRemote
import com.amplifyframework.datastore.generated.model.OpenAi
import com.amplifyframework.datastore.generated.model.TokenManagement
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.hub.HubEvent
import com.amplifyframework.kotlin.core.Amplify
import com.bootsnip.aichat.db.ChatHistory
import com.bootsnip.aichat.db.ChatHistoryDao
import com.bootsnip.aichat.db.ChatHistoryUpdate
import com.bootsnip.aichat.db.ChatHistoryUpdateFav
import com.bootsnip.aichat.db.Tokens
import com.bootsnip.aichat.db.TokensDao
import com.bootsnip.aichat.db.TokensUpdate
import com.bootsnip.aichat.service.IApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)

class AiRepo @Inject constructor(
    private val service: IApiService,
    private val ioDispatcher: CoroutineDispatcher,
    private val chatHistoryDao: ChatHistoryDao,
    private val tokensDao: TokensDao
) : IAiRepo {

    override suspend fun gtpChatResponse(
        query: List<ChatMessage>,
        openAiAuth: String,
        modelId: String
    ): ChatCompletion =
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
            OpenAI(openAiAuth).chatCompletion(
                service.getGPTResponse(
                    completeQuery.toList(),
                    modelId
                )
            )
        }

    //region room db functions
    override fun getAllChatHistory() =
        chatHistoryDao.getAllChatHistoryDistinct()

    override fun getAllFavChatHistory() =
        chatHistoryDao.getAllFavChatHistoryDistinct()

    override suspend fun updateChatHistory(
        chatHistoryUpdate: ChatHistoryUpdate
    ) =
        withContext(ioDispatcher) {
            chatHistoryDao.updateChatHistory(chatHistoryUpdate)
        }

    override suspend fun updateChatHistoryFavStatus(
        chatHistoryUpdateFav: ChatHistoryUpdateFav
    ) =
        withContext(ioDispatcher) {
            chatHistoryDao.updateChatHistoryFavStatus(chatHistoryUpdateFav)
        }

    override fun insertChatHistory(chatHistory: ChatHistory) =
        chatHistoryDao.insertChatHistory(chatHistory)

    override suspend fun deleteChatHistory(id: Int) =
        withContext(ioDispatcher) {
            chatHistoryDao.deleteChatHistory(id)
        }

    override suspend fun insertTokens(tokens: Tokens) =
        withContext(ioDispatcher) {
            tokensDao.insertToken(tokens)
        }

    override fun getTokens(): Flow<List<Tokens>> =
        tokensDao.getTokensDistinct()

    override suspend fun updateLocalTokens(tokensUpdate: TokensUpdate) =
        withContext(ioDispatcher) {
            tokensDao.updateTokens(tokensUpdate)
        }

    //region amplify
    override suspend fun fetchAuthSession(): AuthSession =
        withContext(ioDispatcher) {
            Amplify.Auth.fetchAuthSession()
        }

    override suspend fun observeDataStore(): Flow<DataStoreQuerySnapshot<OpenAi>> =
        withContext(ioDispatcher) {
            Amplify.DataStore.observeQuery(
                OpenAi::class,
                ObserveQueryOptions()
            )
        }

    override suspend fun queryDataStore(): Flow<OpenAi> =
        withContext(ioDispatcher) {
            Amplify.DataStore.query(OpenAi::class)
        }

    override suspend fun observeGPTLLMs(): Flow<DataStoreQuerySnapshot<ChatGPTLLMs>> =
        withContext(ioDispatcher) {
            Amplify.DataStore.observeQuery(
                ChatGPTLLMs::class,
                ObserveQueryOptions()
            )
        }

    override suspend fun queryGPTLLMs(): Flow<ChatGPTLLMs> =
        withContext(ioDispatcher) {
            Amplify.DataStore.query(ChatGPTLLMs::class)
        }

    override suspend fun getCurrentUser(): AuthUser =
        withContext(ioDispatcher) {
            Amplify.Auth.getCurrentUser()
        }

    override suspend fun fetchUserAttributes(): List<AuthUserAttribute> =
        withContext(ioDispatcher) {
            Amplify.Auth.fetchUserAttributes()
        }

    override suspend fun signOut(): AuthSignOutResult =
        withContext(ioDispatcher) {
            val options = AuthSignOutOptions.builder()
                .globalSignOut(true)
                .build()

            Amplify.Auth.signOut(options)
        }

    override suspend fun observeChatHistory(userId: String): Flow<DataStoreItemChange<ChatHistoryRemote>> =
        withContext(ioDispatcher) {
            Amplify.DataStore.observe(
                ChatHistoryRemote::class,
                ChatHistoryRemote.USER_ID.eq(userId)
            )
        }

    override suspend fun queryChatHistory(userId: String): Flow<ChatHistoryRemote> =
        withContext(ioDispatcher) {
            Amplify.DataStore.query(
                ChatHistoryRemote::class,
                Where.matches(ChatHistoryRemote.USER_ID.eq(userId))
            )
        }

    override suspend fun saveChatHistory(chatHistoryRemote: ChatHistoryRemote) {
        withContext(ioDispatcher) {
            try {
                Amplify.DataStore.save(chatHistoryRemote)
                Log.i("Chat History", "Saved a chat history.")
            } catch (error: DataStoreException) {
                Log.e("Chat History", "Save failed.", error)
            }
        }
    }

    override suspend fun observeFavChatHistory(): Flow<DataStoreItemChange<ChatHistoryRemote>> =
        withContext(ioDispatcher) {
            Amplify.DataStore.observe(ChatHistoryRemote::class, ChatHistoryRemote.FAV.gt(1))
        }

    override suspend fun queryFavChatHistory(): Flow<ChatHistoryRemote> =
        withContext(ioDispatcher) {
            Amplify.DataStore.query(
                ChatHistoryRemote::class,
                Where.matches(ChatHistoryRemote.FAV.gt(1))
            )
        }

    override suspend fun dataStoreEventHub(): Flow<HubEvent<*>> =
        Amplify.Hub.subscribe(HubChannel.DATASTORE)
        { it.name == DataStoreChannelEventName.MODEL_SYNCED.toString() }

    override suspend fun saveTokenManagement(tokenManagement: TokenManagement) {
        withContext(ioDispatcher) {
            try {
                Amplify.DataStore.save(tokenManagement)
                Log.i("Token Management", "Saved token management.")
            } catch (error: DataStoreException) {
                Log.e("Token Management", "Save failed.", error)
            }
        }
    }

    override suspend fun updateTokenManagement(tokenManagement: TokenManagement) {
        val whereClause = if(tokenManagement.userId.isNotEmpty())
            TokenManagement.USER_ID.eq(tokenManagement.userId)
        else
            TokenManagement.IDENTITY_ID.eq(tokenManagement.identityId)

        Amplify.DataStore.query(
            TokenManagement::class,
            Where.matches(whereClause)
        )
            .catch { Log.e("Token Management", "Query failed", it) }
            .map {
                it.copyOfBuilder()
                    .userId(tokenManagement.userId)
                    .identityId(tokenManagement.identityId)
                    .unlimited(tokenManagement.unlimited)
                    .promptTokens(tokenManagement.promptTokens)
                    .completionTokens(tokenManagement.completionTokens)
                    .totalTokens(tokenManagement.totalTokens)
                    .build()
            }
            .onEach { Amplify.DataStore.save(it) }
            .catch { Log.e("Token Management", "Update failed", it) }
            .collect { Log.i("Token Management", "Updated a post") }
    }

    override suspend fun observeTokenManagement(userId: String, identityId: String): Flow<DataStoreQuerySnapshot<TokenManagement>> =
        withContext(ioDispatcher) {
            val whereClause = if(userId.isNotEmpty())
                TokenManagement.USER_ID.eq(userId)
            else
                TokenManagement.IDENTITY_ID.eq(identityId)

            Amplify.DataStore.observeQuery(
                TokenManagement::class,
                ObserveQueryOptions(whereClause, null)
            )
        }

    override suspend fun queryTokenManagement(userId: String, identityId: String): Flow<TokenManagement> =
        withContext(ioDispatcher) {
            val whereClause = if(userId.isNotEmpty())
                TokenManagement.USER_ID.eq(userId)
            else
                TokenManagement.IDENTITY_ID.eq(identityId)

            Amplify.DataStore.query(
                TokenManagement::class,
                Where.matches(whereClause)
            )
        }

    override suspend fun observeTokenManagementChanged(
        userId: String,
        identityId: String
    ): Flow<DataStoreItemChange<TokenManagement>> =
        withContext(ioDispatcher) {
            val whereClause = if(userId.isNotEmpty())
                TokenManagement.USER_ID.eq(userId)
            else
                TokenManagement.IDENTITY_ID.eq(identityId)

            Amplify.DataStore.observe(
                TokenManagement::class,
                whereClause
            )
        }


    override suspend fun updateChatHistoryRemote(chatHistoryRemote: ChatHistoryRemote) {
        Amplify.DataStore.query(
            ChatHistoryRemote::class,
            Where.matches(ChatHistoryRemote.LOCAL_DB_ID.eq(chatHistoryRemote.localDbId))
        )
            .catch { Log.e("Chat History", "Query failed", it) }
            .map {
                it.copyOfBuilder()
                    .assistantType(chatHistoryRemote.assistantType)
                    .chatMessageList(chatHistoryRemote.chatMessageList)
                    .userId(chatHistoryRemote.userId)
                    .fav(chatHistoryRemote.fav)
                    .localDbId(chatHistoryRemote.localDbId)
                    .build()
            }
            .onEach { Amplify.DataStore.save(it) }
            .catch { Log.e("Chat History", "Update failed", it) }
            .collect { Log.i("Chat History", "Updated a post") }
    }

    override suspend fun updateChatHistoryUserId(userId: String, id: String) {
        Amplify.DataStore.query(
            ChatHistoryRemote::class,
            Where.identifier(ChatHistoryRemote::class.java, id)
        )
            .catch { Log.e("Chat History", "Query failed", it) }
            .map {
                it.copyOfBuilder()
                    .userId(userId)
                    .build()
            }
            .onEach { Amplify.DataStore.save(it) }
            .catch { Log.e("Chat History", "Update failed", it) }
            .collect { Log.i("Chat History", "Updated a post") }
    }
}