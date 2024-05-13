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
import com.amplifyframework.datastore.generated.model.ChatHistoryRemote
import com.amplifyframework.datastore.generated.model.ChatMessageObject
import com.bootsnip.aichat.repo.IAiRepo
import com.bootsnip.aichat.util.AssistantType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AstraViewModel @Inject constructor(
    private val repo: IAiRepo,
    application: Application
) : AndroidViewModel(application) {

    val chatList: MutableStateFlow<MutableList<ChatMessage>> = MutableStateFlow(mutableListOf())

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _chatHistoryRemote: MutableStateFlow<List<ChatHistoryRemote>> =
        MutableStateFlow(mutableListOf())
    val chatHistoryRemote = _chatHistoryRemote.asStateFlow()

    private val _favChatHistoryRemote: MutableStateFlow<List<ChatHistoryRemote>> =
        MutableStateFlow(mutableListOf())
    val favChatHistoryRemote = _favChatHistoryRemote.asStateFlow()

    private val currentRowId: MutableStateFlow<String?> = MutableStateFlow(null)

    val selectedChatHistory: MutableStateFlow<String?> = MutableStateFlow(null)

    private val openAiAuth: MutableStateFlow<String> = MutableStateFlow("")

    private val currentUserId: MutableStateFlow<String?> = MutableStateFlow(null)

    private var isUpdate: Boolean = false

    init {
        fetchAuthSession()
        AuthFetchSessionOptions.builder().forceRefresh(true).build()
        queryOpenAi()
        if (openAiAuth.value.isEmpty()) observeOpenAi()
        getCurrentUser()
        observeChatHistory()
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

                insertOrUpdateDbRemote()

                Log.d("GPT RESPONSE ID", response.id)
                _isLoading.value = false

            } catch (e: Exception) {
                //Handle error scenario
                _isLoading.value = false
            }
        }
    }


    private fun insertOrUpdateDbRemote() {
        val chatList = chatList.value.map { chatMessage ->
            ChatMessageObject.builder()
                .role(chatMessage.role.role)
                .content(chatMessage.content)
                .build()
        }

        if (isUpdate) {
            updateChatHistory(chatList)
        } else {
            val chatHistoryRemote = ChatHistoryRemote.builder()
                .assistantType(AssistantType.GPT35TURBO.assistantType)
                .userId(currentUserId.value ?: "")
                .fav(0)
                .chatMessageList(chatList)
                .build()
            saveChatHistory(chatHistoryRemote)
        }
        queryChatHistory()
        observeChatHistory()
    }

    fun startNewChat() {
        chatList.value = mutableListOf()
        currentRowId.value = ""
    }

    fun continueChat(uid: String) {
        chatList.value = mutableListOf()
        queryChatHistory()

        val chatHistory = chatHistoryRemote.value.find {
            it.id == uid
        }

        val list = chatHistory?.chatMessageList?.toMutableList()?.map {
            ChatMessage(
                ChatRole(it.role),
                it.content
            )
        }?.toMutableList() ?: mutableListOf()

        Log.i("ChatHistory List", list.toString())

        chatList.value = list
        currentRowId.value = uid
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

    private fun getCurrentUser() {
        viewModelScope.launch {
            try {
                val response = repo.getCurrentUser()
                currentUserId.value = response.userId
            } catch (e: Exception) {
                Log.e("SignedOut", e.message.toString())
            }

        }
    }

    fun observeChatHistory() {
        viewModelScope.launch {
            val list = mutableListOf<ChatHistoryRemote>()
            val response = repo.observeChatHistory()
            response
                .catch { Log.e("Chat History", "Error querying chat history", it) }
                .collectLatest { list.add(it.item()) }
            _chatHistoryRemote.value = list.toList()
        }
    }

    fun queryChatHistory() {
        viewModelScope.launch {
            val list = mutableListOf<ChatHistoryRemote>()
            val response = repo.queryChatHistory()
            response
                .catch { Log.e("Chat History", "Error querying chat history", it) }
                .collect { list.add(it) }
            _chatHistoryRemote.value = list.toList()
        }
    }

    private fun saveChatHistory(chatHistoryRemote: ChatHistoryRemote) {
        viewModelScope.launch {
            currentRowId.value = chatHistoryRemote.id
            repo.saveChatHistory(chatHistoryRemote)
        }
    }

    private fun updateChatHistory(chatList: List<ChatMessageObject>) {
        viewModelScope.launch {
            currentRowId.value?.let {
                repo.updateChatHistory(chatList, it)
            }
        }
    }

}