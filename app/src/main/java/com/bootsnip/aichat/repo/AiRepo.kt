package com.bootsnip.aichat.repo

import android.util.Log
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.client.OpenAI
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.auth.AuthUser
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.DataStoreException
import com.amplifyframework.datastore.DataStoreItemChange
import com.amplifyframework.datastore.generated.model.ChatHistoryRemote
import com.amplifyframework.datastore.generated.model.ChatMessageObject
import com.amplifyframework.datastore.generated.model.OpenAi
import com.amplifyframework.kotlin.core.Amplify
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
    private val ioDispatcher: CoroutineDispatcher
) : IAiRepo {
    override suspend fun gtpChatResponse(
        query: List<ChatMessage>,
        openAiAuth: String
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
            OpenAI(openAiAuth).chatCompletion(service.getGPTResponse(completeQuery.toList()))
        }


    //region amplify
    override suspend fun fetchAuthSession(): AuthSession =
        withContext(ioDispatcher) {
            Amplify.Auth.fetchAuthSession()
        }

    override suspend fun observeDataStore(): Flow<DataStoreItemChange<OpenAi>> =
        withContext(ioDispatcher) {
            Amplify.DataStore.observe(OpenAi::class)
        }

    override suspend fun queryDataStore(): Flow<OpenAi> =
        withContext(ioDispatcher) {
            Amplify.DataStore.query(OpenAi::class)
        }

    override suspend fun getCurrentUser(): AuthUser =
        withContext(ioDispatcher) {
            Amplify.Auth.getCurrentUser()
        }

    override suspend fun observeChatHistory(): Flow<DataStoreItemChange<ChatHistoryRemote>> =
        withContext(ioDispatcher) {
            Amplify.DataStore.observe(ChatHistoryRemote::class)
        }

    override suspend fun queryChatHistory(): Flow<ChatHistoryRemote> =
        withContext(ioDispatcher) {
            Amplify.DataStore.query(ChatHistoryRemote::class)
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
        withContext(ioDispatcher){
            Amplify.DataStore.observe(ChatHistoryRemote::class, ChatHistoryRemote.FAV.gt(1))
        }

    override suspend fun queryFavChatHistory(): Flow<ChatHistoryRemote> =
        withContext(ioDispatcher) {
            Amplify.DataStore.query(ChatHistoryRemote::class, Where.matches(ChatHistoryRemote.FAV.gt(1)))
        }

    override suspend fun updateChatHistory(chatList: List<ChatMessageObject>, id: String) {
        Amplify.DataStore.query(ChatHistoryRemote::class, Where.identifier(ChatHistoryRemote::class.java, id))
            .catch { Log.e("Chat History", "Query failed", it) }
            .map {
                it.copyOfBuilder()
                    .chatMessageList(chatList)
                    .build()
            }
            .onEach { Amplify.DataStore.save(it) }
            .catch { Log.e("Chat History", "Update failed", it) }
            .collect { Log.i("Chat History", "Updated a post") }
    }
}