package com.bootsnip.aichat.repo

import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.client.OpenAI
import com.amplifyframework.auth.AuthSession
import com.amplifyframework.datastore.DataStoreItemChange
import com.amplifyframework.datastore.generated.model.OpenAi
import com.amplifyframework.kotlin.core.Amplify
import com.bootsnip.aichat.db.ChatHistory
import com.bootsnip.aichat.db.ChatHistoryDao
import com.bootsnip.aichat.db.ChatHistoryUpdate
import com.bootsnip.aichat.db.ChatHistoryUpdateFav
import com.bootsnip.aichat.service.IApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)

class AiRepo @Inject constructor(
    private val service: IApiService,
    private val ioDispatcher: CoroutineDispatcher,
    private val chatHistoryDao: ChatHistoryDao
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

    fun chatHistoryRemoteOps() {
       /* val item: ChatHistoryRemote = ChatHistoryRemote.builder()
            .uid(1020)
            .assistant_type("Lorem ipsum dolor sit amet")
            .chat_message_list([])
            .fav(1020)
            .user_id("a3f4095e-39de-43d2-baf4-f8c16f0f6f4d")
            .build()
        Amplify.DataStore.save(
            item,
            { success -> Log.i("Amplify", "Saved item: " + success.item().name) },
            { error -> Log.e("Amplify", "Could not save item to DataStore", error) }
        )

        Amplify.DataStore.save(
            updatedItem,
            { success -> Log.i("Amplify", "Updated item: " + success.item().name) },
            { error -> Log.e("Amplify", "Could not save item to DataStore", error) }
        )

        Amplify.DataStore.delete(toDeleteItem,
            { deleted -> Log.i("Amplify", "Deleted item.") },
            { failure -> Log.e("Amplify", "Delete failed.", failure) }
        )

        Amplify.DataStore.query(
            ChatHistoryRemote::class.java,
            { items ->
                while (items.hasNext()) {
                    val item = items.next()
                    Log.i("Amplify", "Queried item: " + item.id)
                }
            },
            { failure -> Log.e("Tutorial", "Could not query DataStore", failure) }
        )*/
    }
}