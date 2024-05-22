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
import com.amplifyframework.datastore.generated.model.ChatGPTLLMs
import com.amplifyframework.datastore.generated.model.ChatHistoryRemote
import com.amplifyframework.datastore.generated.model.ChatMessageObject
import com.amplifyframework.datastore.generated.model.Tokens
import com.bootsnip.aichat.db.ChatHistory
import com.bootsnip.aichat.db.ChatHistoryUpdate
import com.bootsnip.aichat.db.ChatHistoryUpdateFav
import com.bootsnip.aichat.db.TokensUpdate
import com.bootsnip.aichat.model.AstraChatMessage
import com.bootsnip.aichat.repo.IAiRepo
import com.bootsnip.aichat.util.AssistantType
import com.bootsnip.aichat.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Timestamp
import javax.inject.Inject

@HiltViewModel
class AstraViewModel @Inject constructor(
    private val repo: IAiRepo,
    application: Application
) : AndroidViewModel(application) {

    val chatList: MutableStateFlow<MutableList<ChatMessage>> = MutableStateFlow(mutableListOf())

    val errorChatList: MutableStateFlow<MutableList<ChatMessage>> =
        MutableStateFlow(mutableListOf())

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

    private val _gptLLMs: MutableStateFlow<MutableList<ChatGPTLLMs>> =
        MutableStateFlow(mutableListOf())
    val gptLLMs = _gptLLMs.asStateFlow()

    private val currentUserId: MutableStateFlow<String?> = MutableStateFlow(null)

    private val identity: MutableStateFlow<String?> = MutableStateFlow(null)

    private val isSignedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _tokensLocal: MutableStateFlow<List<com.bootsnip.aichat.db.Tokens>> =
        MutableStateFlow(mutableListOf())
    val tokensLocal = _tokensLocal.asStateFlow()

    private val tokensRemote: MutableStateFlow<Tokens?> = MutableStateFlow(null)

    private val _tokensError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val tokensError = _tokensError.asStateFlow()

    private val _tokensCount: MutableStateFlow<Int> = MutableStateFlow(3)
    val tokensCount = _tokensCount.asStateFlow()

    val showPurchaseScreen: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _selectedGPTLLM: MutableStateFlow<ChatGPTLLMs?> = MutableStateFlow(null)
    val selectedGPTLLM = _selectedGPTLLM.asStateFlow()

    private var isUpdate: Boolean = false

    private val _selectedResponse: MutableStateFlow<String> = MutableStateFlow("")
    val selectedResponse = _selectedResponse.asStateFlow()

    private val _selectedQuery: MutableStateFlow<String> = MutableStateFlow("")
    val selectedQuery = _selectedQuery.asStateFlow()

    private val _isQuerySelected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isQuerySelected = _isQuerySelected.asStateFlow()

    private val _isResponseSelected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isResponseSelected = _isResponseSelected.asStateFlow()

    init {
        observeOpenAI()
        prepareOpenAI()
        fetchSignInState()
        getAllChatHistory()
        getLocalToken()
        resolveTokenCount()
    }

    fun getGPTResponse(gptQuery: String) {
        showPurchaseScreen.value = tokensError.value
        _isLoading.value = true
        //check internet connection here.
        isLLMAvailable(gptQuery)
        //need to change this to avoid a crash. Have default values
        if(tokensDepleted(gptQuery)) return

        viewModelScope.launch {
            try {
                isUpdate = chatList.value.isNotEmpty()

                val newQueryList = chatList.value.toMutableList()

                chatList.value = newQueryList.apply {
                    this.add(
                        ChatMessage(ChatRole.User, gptQuery)
                    )
                }

                val response = repo.gtpChatResponse(
                    newQueryList.toList(),
                    openAiAuth.value,
                    selectedGPTLLM.value?.llmVersion ?: "gpt-3.5-turbo"
                )

                val message = response.choices.first().message.content.orEmpty()
                val role = response.choices.first().message.role

                val newResponseList = chatList.value.toMutableList()
                chatList.value = newResponseList.apply {
                    this.add(
                        ChatMessage(role, message)
                    )
                }

                insertOrUpdateChatHistoryDb()
                updateLocalTokenAfterQuery()

                Log.d("GPT RESPONSE ID", response.id)
                _isLoading.value = false

            } catch (e: Exception) {
                //Handle error scenario
                _isLoading.value = false
            }
        }
    }

    private fun prepareOpenAI(retryGPTQuery: Boolean = false, gptQuery: String = "") {
        viewModelScope.launch {
            val set = gptLLMs.value.toMutableSet()
            val openAi = async { repo.queryDataStore() }.await()
            val gptVersion = async { repo.queryGPTLLMs() }.await()

            openAi
                .catch { Log.e("OpenAI KEY", "Error querying key", it) }
                .collect {
                    Log.i("OpenAI KEY", "key ${it.openAi}")
                    openAiAuth.value = it.openAi
                    if(retryGPTQuery && openAiAuth.value.isNotEmpty()) {
                        getGPTResponse(gptQuery)
                    }
                }

            gptVersion
                .catch { Log.e("GPT LLM List", "Error querying GPT LLM List", it) }
                .collect {
                    Log.i("GPT LLM List", "llm ${it.llmVersion}")
                    set.add(it)
                }
            _gptLLMs.value = set.toMutableList()
            _selectedGPTLLM.value = gptLLMs.value.firstOrNull()
        }
    }

    private fun observeOpenAI() {
        viewModelScope.launch {
            val set = gptLLMs.value.toMutableSet()
            val openAi = async { repo.observeDataStore() }.await()
            val gptVersion = async { repo.observeGPTLLMs() }.await()

            openAi
                .catch { Log.e("OpenAI KEY", "Error querying key", it) }
                .collect {
                    Log.i("OpenAI KEY", "key ${it.item().openAi}")
                    openAiAuth.value = it.item().openAi
                }

            gptVersion
                .catch { Log.e("GPT LLM List", "Error observing GPT LLM List", it) }
                .collect {
                    Log.i("GPT LLM List", "key ${it.item().llmVersion}")
                    set.add(it.item())
                }

            _gptLLMs.value = set.toMutableList()
            _selectedGPTLLM.value = gptLLMs.value.firstOrNull()
        }
    }

    private fun isLLMAvailable(query: String){
        val isLLMAvailable = openAiAuth.value.isNotEmpty()
        if(!isLLMAvailable){
            prepareOpenAI(retryGPTQuery = true, gptQuery = query)
        }
    }

    private fun tokensDepleted(query: String): Boolean {
        val remainingTokenCount = tokensLocal.value.firstOrNull()?.remainingCount ?: 0
        val tokenUnlimited = tokensLocal.value.firstOrNull()?.unlimited ?: false
        val tokensDepletedStatus = remainingTokenCount <= 0 && !tokenUnlimited
        if(tokensDepletedStatus) {
            _tokensError.value = true
            _isLoading.value = false
            showPurchaseScreen.value = tokensError.value
            getChatListWithError(query)
        }
        return tokensDepletedStatus
    }

    private fun insertOrUpdateChatHistoryDb() {
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

    private fun resolveTokenCount() {
        if (isSignedIn.value) {
            if (tokensLocal.value.isEmpty()) {
                val remoteToken = tokensRemote.value
                if (remoteToken != null) {
                    if (remoteToken.unlimited) {
                        insertFreshToken(true)
                    } else {
                        insertFreshToken()
                    }
                } else {
                    insertFreshToken()
                }
            }
        } else {
            if (tokensLocal.value.isEmpty()) {
                insertFreshToken()
            }
        }
    }

    private fun insertFreshToken(unlimited: Boolean = false) {
        val token = com.bootsnip.aichat.db.Tokens(
            remainingCount = 3,
            unlimited = unlimited,
            date = DateUtil.currentDate()
        )
        insertLocalToken(token)
    }

    private fun insertLocalToken(tokens: com.bootsnip.aichat.db.Tokens) {
        viewModelScope.launch {
            repo.insertTokens(tokens)
            getLocalToken()
        }
    }

    private fun refreshDaysToken() {
        val tokenLocal = tokensLocal.value[0]
        if (!tokenLocal.unlimited) {
            if (tokenLocal.date != DateUtil.currentDate()) {
                val tokensUpdate = TokensUpdate(
                    tokenLocal.uid,
                    3,
                    false,
                    DateUtil.currentDate()
                )
                updateLocalToken(tokensUpdate)
            }
            Log.i("DATE", tokenLocal.date)
            Log.i("DATE", DateUtil.currentDate())
        }
    }

    private fun getLocalToken() {
        viewModelScope.launch {
            repo.getTokens().collectLatest {
                _tokensLocal.value = it
                if (tokensLocal.value.isNotEmpty()) {
                    _tokensCount.value = tokensLocal.value[0].remainingCount
                    refreshDaysToken()
                }
            }
        }
    }

    private fun updateLocalTokenAfterQuery() {
        val tokenLocal = tokensLocal.value[0]
        if (!tokenLocal.unlimited) {
            val tokensUpdate = TokensUpdate(
                tokenLocal.uid,
                tokenLocal.remainingCount - 1,
                false,
                DateUtil.currentDate()
            )
            updateLocalToken(tokensUpdate)
        }
    }

    private fun updateLocalToken(tokensUpdate: TokensUpdate) {
        viewModelScope.launch {
            try {
                repo.updateLocalTokens(tokensUpdate)
                getLocalToken()
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
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
        errorChatList.value = mutableListOf()
        currentRowId.value = null
    }

    fun continueChat(uid: Int) {
        _tokensError.value = false
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

    private fun fetchSignInState() {
        viewModelScope.launch {
            val response = repo.fetchAuthSession() as AWSCognitoAuthSession
            isSignedIn.value = response.isSignedIn
            fetchCurrentUser()
        }
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            try {
                val response = repo.getCurrentUser()
                currentUserId.value = response.userId
            } catch (e: Exception) {
                Log.e("SignedOut", e.message.toString() + " ${currentUserId.value}")
            }
        }
    }

    fun fetchChatHistoryWithAuthSession() {
        viewModelScope.launch {
            val response = repo.fetchAuthSession() as AWSCognitoAuthSession
            isSignedIn.value = response.isSignedIn
            if (isSignedIn.value) getChatHistoryWithCurrentUser()
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

    private fun getChatHistoryWithCurrentUser() {
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
            val chatHistoryRemoteFound =
                chatHistoryRemote.value.find { it.localDbId == chatHistoryLocal.cloudSyncId }

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

            if (chatHistoryRemoteFound != null) {
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

    private fun getChatListWithError(gptQuery: String) {
        viewModelScope.launch {
            if (tokensError.value) {
                val newList = chatList.value.toMutableList()
                errorChatList.value = newList.apply {
                    this.add(
                        ChatMessage(
                            ChatRole.User,
                            gptQuery
                        )
                    )
                    this.add(
                        ChatMessage(
                            ChatRole.Assistant,
                            "Oops, you've run out of tokens for today! Please purchase a subscription to continue enjoying Astra AI."
                        )
                    )
                }
            }
        }
    }

    fun setSelectedLLM(llMVersion: String) {
        val selectLLM = gptLLMs.value.find { it.llmVersion == llMVersion }
        _selectedGPTLLM.value = selectLLM
    }

    fun setSelectedResponse(selectedResponse: String) {
        _selectedResponse.value = selectedResponse
        _isResponseSelected.value = true
        _isQuerySelected.value = false
    }

    fun setSelectedQuery(selectedQuery: String) {
        _selectedQuery.value = selectedQuery
        _isQuerySelected.value = true
        _isResponseSelected.value = false
    }
}