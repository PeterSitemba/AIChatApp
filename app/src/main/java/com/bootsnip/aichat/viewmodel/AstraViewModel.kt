package com.bootsnip.aichat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.options.AuthFetchSessionOptions
import com.amplifyframework.auth.result.AuthSessionResult
import com.bootsnip.aichat.db.ChatHistory
import com.bootsnip.aichat.db.ChatHistoryUpdate
import com.bootsnip.aichat.db.ChatHistoryUpdateFav
import com.bootsnip.aichat.model.AstraChatMessage
import com.bootsnip.aichat.repo.IAiRepo
import com.bootsnip.aichat.util.AssistantType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AstraViewModel @Inject constructor(
    private val repo: IAiRepo,
    application: Application
) : AndroidViewModel(application) {

    val chatList: MutableStateFlow<MutableList<ChatMessage>> = MutableStateFlow(mutableListOf())

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _chatHistory: MutableStateFlow<List<ChatHistory>> =
        MutableStateFlow(mutableListOf())
    val chatHistory = _chatHistory.asStateFlow()

    private val _favChatHistory: MutableStateFlow<List<ChatHistory>> =
        MutableStateFlow(mutableListOf())
    val favChatHistory = _favChatHistory.asStateFlow()

    private val currentRowId: MutableStateFlow<Long?> = MutableStateFlow(null)

    val selectedChatHistory: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val openAiAuth: MutableStateFlow<String> = MutableStateFlow("")

    private var isUpdate: Boolean = false

    init {
        fetchAuthSession()
        AuthFetchSessionOptions.builder().forceRefresh(true).build()
        queryOpenAi()
        if(openAiAuth.value.isEmpty()) observeOpenAi()
    }

    fun getGPTResponse(gptQuery: String) {
        _isLoading.value = true
        if (openAiAuth.value.isEmpty()) {
            queryOpenAi()
            //close loading indicator and display no internet connection error
            return
        }
        viewModelScope.launch {
            try {
                isUpdate = chatList.value.isNotEmpty()

                val newQueryList = chatList.value.toMutableList()
                chatList.value = newQueryList.apply {
                    this.add(
                        ChatMessage(ChatRole.User, gptQuery)
                    )
                }

                val response = repo.gtpChatResponse(newQueryList.toList(), openAiAuth.value)
                val message = response.choices.first().message.content.orEmpty()
                val role = response.choices.first().message.role

                val newResponseList = chatList.value.toMutableList()
                chatList.value = newResponseList.apply {
                    this.add(
                        ChatMessage(role, message)
                    )
                }

                insertOrUpdateDb()

                Log.d("GPT RESPONSE ID", response.id)
                _isLoading.value = false

            } catch (e: Exception) {
                //Handle error scenario
                _isLoading.value = false
            }
        }
    }

    private fun insertOrUpdateDb() {
        val chatList = chatList.value.map { chatMessage ->
            AstraChatMessage(
                chatMessage.role,
                chatMessage.content
            )
        }

        if (isUpdate) {
            val chatHistoryUpdate = ChatHistoryUpdate(
                currentRowId.value?.toInt(),
                chatList
            )
            updateChatHistory(chatHistoryUpdate)
        } else {
            val chatHistory = ChatHistory(
                assistantType = AssistantType.GPT35TURBO.assistantType,
                chatMessageList = chatList,
                fav = 0
            )
            insertChatHistory(chatHistory)
        }
        getAllChatHistory()
    }

    private fun insertChatHistory(chatHistory: ChatHistory) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                currentRowId.value = repo.insertChatHistory(chatHistory)
            }
        }
    }

    fun getAllChatHistory() {
        viewModelScope.launch {
            repo.getAllChatHistory().collectLatest {
                _chatHistory.value = it
            }
        }
    }

    fun getFavChatHistory() {
        viewModelScope.launch {
            repo.getAllFavChatHistory().collectLatest {
                _favChatHistory.value = it
            }
        }
    }

    private fun updateChatHistory(chatHistoryUpdate: ChatHistoryUpdate) {
        viewModelScope.launch {
            repo.updateChatHistory(chatHistoryUpdate)
        }
    }

    fun updateChatHistoryStatus(chatHistoryUpdateFav: ChatHistoryUpdateFav) {
        viewModelScope.launch {
            repo.updateChatHistoryFavStatus(chatHistoryUpdateFav)
        }
    }

    fun deleteChatHistory(id: Int) {
        viewModelScope.launch {
            repo.deleteChatHistory(id)
        }
    }

    fun startNewChat() {
        chatList.value = mutableListOf()
        currentRowId.value = null
    }

    fun continueChat(uid: Int) {
        val chatHistory = chatHistory.value.find {
            it.uid == uid
        }

        chatList.value = chatHistory?.chatMessageList?.map {
            ChatMessage(
                it.role,
                it.content
            )
        }?.toMutableList() ?: mutableListOf()

        currentRowId.value = uid.toLong()
    }

    private fun queryOpenAi() {
        viewModelScope.launch {
            val response = repo.queryDataStore()
            response
                .catch { Log.e("OpenAI KEY", "Error querying key", it) }
                .collect {
                    Log.i("OpenAI KEY", "key ${it.openAi}")
                    openAiAuth.value = it.openAi
                }
        }
    }

    private fun observeOpenAi() {
        viewModelScope.launch {
            val response = repo.observeDataStore()
            response
                .catch { Log.e("OpenAI KEY", "Error querying key", it) }
                .collect {
                    Log.i("OpenAI KEY", "key ${it.item().openAi}")
                    openAiAuth.value = it.item().openAi
                }
        }
    }

    private fun fetchAuthSession() {
        viewModelScope.launch {
            val response = repo.fetchAuthSession() as AWSCognitoAuthSession
            when (response.identityIdResult.type) {
                AuthSessionResult.Type.SUCCESS -> {
                    Log.i("Auth Session", "IdentityId = ${response.identityIdResult.value}")
                    Log.i(
                        "Auth Session",
                        "access Token = ${response.tokensResult.value?.accessToken}"
                    )
                }
                AuthSessionResult.Type.FAILURE ->
                    Log.w("Auth Session", "IdentityId not found", response.identityIdResult.error)
            }
        }
    }

}