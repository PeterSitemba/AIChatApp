package com.bootsnip.aichat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.generated.model.ChatHistoryRemote
import com.amplifyframework.datastore.generated.model.ChatMessageObject
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
import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date
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

    private val _chatHistoryRemote: MutableStateFlow<List<ChatHistoryRemote>> =
        MutableStateFlow(mutableListOf())
    val chatHistoryRemote = _chatHistoryRemote.asStateFlow()

    private val currentRowId: MutableStateFlow<Long?> = MutableStateFlow(null)

    val selectedChatHistory: MutableStateFlow<Int?> = MutableStateFlow(null)

    private val openAiAuth: MutableStateFlow<String> = MutableStateFlow("")

    private val currentUserId: MutableStateFlow<String?> = MutableStateFlow(null)

    private val identity: MutableStateFlow<String?> = MutableStateFlow(null)

    private val isSignedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var isUpdate: Boolean = false

    init {
        queryOpenAi()
        //fetchAuthSession()
        if (openAiAuth.value.isEmpty()) observeOpenAi()
        getAllChatHistory()
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
                chatList,
                System.currentTimeMillis()
            )
            updateChatHistory(chatHistoryUpdate)
        } else {
            val chatHistory = ChatHistory(
                assistantType = AssistantType.GPT35TURBO.assistantType,
                chatMessageList = chatList,
                fav = 0,
                modifiedAt = System.currentTimeMillis()
            )
            insertChatHistory(chatHistory)
        }
        getAllChatHistory()
    }

    private fun insertChatHistory(chatHistory: ChatHistory) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                currentRowId.value = repo.insertChatHistory(chatHistory)
                getAllChatHistory()
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

    fun fetchAuthSession() {
        viewModelScope.launch {
            val response = repo.fetchAuthSession() as AWSCognitoAuthSession
            isSignedIn.value = response.isSignedIn
            if (isSignedIn.value) getCurrentUser()
            Log.i("IS SIGNED IN", isSignedIn.value.toString())
            when (response.identityIdResult.type) {
                AuthSessionResult.Type.SUCCESS -> {
                    identity.value = response.identityIdResult.value
                    Log.i("Auth Session", "IdentityId = ${response.identityIdResult.value}")
                    Log.i(
                        "Auth Session",
                        "access Token = ${response.tokensResult.value?.accessToken}"
                    )
                }

                AuthSessionResult.Type.FAILURE -> {
                    identity.value = null
                    Log.w("Auth Session", "IdentityId not found", response.identityIdResult.error)
                }

            }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            try {
                val response = repo.getCurrentUser()
                currentUserId.value = response.userId
                if (currentUserId.value != null) {
                    Log.i("CURRENT USER", currentUserId.value.toString())
                    queryChatHistory()
                }
            } catch (e: Exception) {
                Log.e("SignedOut", e.message.toString() + " ${currentUserId.value}")
            }
        }
    }

    private fun syncChatHistory() {
        if (isSignedIn.value) {
            getAllChatHistory()
            syncChatHistoryDownStream()
            syncChatHistoryUpstream()
        }
    }

    //TODO("Add timestamp from remote db")
    private fun syncChatHistoryDownStream() {
        if (chatHistoryRemote.value.isEmpty()) return
        for (chatHistoryRemote in chatHistoryRemote.value) {
            Log.i("SYNC CHAT HISTORY DOWNSTREAM", chatHistoryRemote.toString())
            val chatHistoryLocal =
                chatHistory.value.find { it.cloudSyncId == chatHistoryRemote.localDbId }

            val chatList = chatHistoryRemote.chatMessageList.map {
                AstraChatMessage(
                    ChatRole(it.role),
                    it.content
                )
            }

            if (chatHistory.value.isEmpty() || chatHistoryLocal == null) {

                insertChatHistory(
                    ChatHistory(
                        assistantType = chatHistoryRemote.assistantType,
                        chatMessageList = chatList,
                        fav = chatHistoryRemote.fav,
                        cloudSyncId = chatHistoryRemote.localDbId,
                        modifiedAt = chatHistoryRemote.modifiedAt.secondsSinceEpoch
                    )
                )
            }
        }
    }

    private fun syncChatHistoryUpstream() {
        if (chatHistory.value.isEmpty()) return

        for (chatHistoryLocal in chatHistory.value) {
            Log.i("CHAT LOCAL", chatHistoryLocal.toString())
            val chatHistoryRemoteFound = chatHistoryRemote.value.find { it.localDbId == chatHistoryLocal.cloudSyncId }

            val chatHistoryRemote = ChatHistoryRemote.builder()
                .assistantType(chatHistoryLocal.assistantType)
                .userId(currentUserId.value)
                .fav(chatHistoryLocal.fav)
                .localDbId(chatHistoryLocal.cloudSyncId)
                .modifiedAt(Temporal.Timestamp(Timestamp(chatHistoryLocal.modifiedAt)))
                .chatMessageList(
                    chatHistoryLocal.chatMessageList?.map {
                        ChatMessageObject.builder()
                            .role(it.role.role)
                            .content(it.content)
                            .build()
                    }
                )
                .build()

            if(chatHistoryRemoteFound != null) {
                updateChatHistoryRemote(chatHistoryRemote)
            } else {
                saveChatHistory(chatHistoryRemote)
            }
        }
    }


    private fun observeChatHistory() {
        viewModelScope.launch {
            val list = mutableListOf<ChatHistoryRemote>()
            val response = repo.observeChatHistory(currentUserId.value!!)
            response
                .catch { Log.e("Queried Chat", "Error querying chat history", it) }
                .collectLatest {
                    list.add(it.item())
                    Log.i("Chat List", it.item().chatMessageList.first().content)
                }
            _chatHistoryRemote.value = list.toList()
            syncChatHistory()
        }
    }

    private fun queryChatHistory() {
        viewModelScope.launch {
            val list = mutableListOf<ChatHistoryRemote>()
            val response = repo.queryChatHistory(currentUserId.value!!)

            response
                .catch { Log.e("Queried Chat", "Error querying chat history", it) }
                .collect {
                    list.add(it)
                    Log.i("Chat List", it.chatMessageList.first().content)
                }
            _chatHistoryRemote.value = list.toList()
            syncChatHistory()
        }
    }

    private fun updateChatHistoryRemote(chatHistoryRemote: ChatHistoryRemote) {
        viewModelScope.launch {
            repo.updateChatHistoryRemote(chatHistoryRemote)
        }
    }

    private fun saveChatHistory(chatHistoryRemote: ChatHistoryRemote) {
        viewModelScope.launch {
            repo.saveChatHistory(chatHistoryRemote)
        }
    }

    /*




        private fun updateChatHistoryUserId(userId: String?) {
            viewModelScope.launch {
                userId?.let {
                    for(chatHistory in chatHistoryRemote.value){
                        if(chatHistory.userId.isNotEmpty()) continue
                        repo.updateChatHistoryUserId(it, chatHistory.id)
                    }
                }
            }
        }*/
}