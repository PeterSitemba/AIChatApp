package com.bootsnip.aichat.viewmodel

import android.app.Application
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.file.FileSource
import com.aallam.openai.api.image.ImageCreation
import com.aallam.openai.api.image.ImageSize
import com.aallam.openai.api.image.ImageVariation
import com.aallam.openai.api.model.ModelId
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.core.model.temporal.Temporal
import com.amplifyframework.datastore.events.ModelSyncedEvent
import com.amplifyframework.datastore.generated.model.ChatGPTLLMs
import com.amplifyframework.datastore.generated.model.ChatHistoryRemote
import com.amplifyframework.datastore.generated.model.ChatMessageObject
import com.amplifyframework.datastore.generated.model.Suggestions
import com.amplifyframework.datastore.generated.model.TokenManagement
import com.bootsnip.aichat.db.ChatHistory
import com.bootsnip.aichat.db.ChatHistoryUpdate
import com.bootsnip.aichat.db.ChatHistoryUpdateFav
import com.bootsnip.aichat.db.TokensUpdate
import com.bootsnip.aichat.model.AstraChatMessage
import com.bootsnip.aichat.repo.IAstraRepo
import com.bootsnip.aichat.util.AssistantType
import com.bootsnip.aichat.util.DateUtil
import com.bootsnip.aichat.util.saveImageAndRetrieveUri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.source
import java.io.InputStream
import java.sql.Timestamp
import javax.inject.Inject

@HiltViewModel
class AstraViewModel @Inject constructor(
    private val repo: IAstraRepo,
    application: Application
) : AndroidViewModel(application) {

    val chatList: MutableStateFlow<MutableList<AstraChatMessage>> =
        MutableStateFlow(mutableListOf())

    val errorChatList: MutableStateFlow<MutableList<AstraChatMessage>> =
        MutableStateFlow(mutableListOf())

    private val _isGPTResponseLoading = MutableStateFlow(false)
    val isGPTResponseLoading = _isGPTResponseLoading.asStateFlow()

    private val _isImagePrompt = MutableStateFlow(false)
    val isImagePrompt = _isImagePrompt.asStateFlow()

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

    private val openAiSynced: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val _gptLLMs: MutableStateFlow<MutableList<ChatGPTLLMs>> =
        MutableStateFlow(mutableListOf())
    val gptLLMs = _gptLLMs.asStateFlow()

    private val currentUserId: MutableStateFlow<String?> = MutableStateFlow(null)

    private val _userName: MutableStateFlow<String?> = MutableStateFlow(null)
    val userName = _userName.asStateFlow()

    private val identity: MutableStateFlow<String?> = MutableStateFlow(null)

    private val _isSignedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSignedIn = _isSignedIn.asStateFlow()

    private val _showSignInSuccessSnackBar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showSignInSuccessSnackBar = _showSignInSuccessSnackBar.asStateFlow()

    private val _showSignOutSuccessSnackBar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showSignOutSuccessSnackBar = _showSignOutSuccessSnackBar.asStateFlow()

    private val _showSignOutFailedSnackBar: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showSignOutFailedSnackBar = _showSignOutFailedSnackBar.asStateFlow()

    private val _tokensLocal: MutableStateFlow<List<com.bootsnip.aichat.db.Tokens>> =
        MutableStateFlow(mutableListOf())
    val tokensLocal = _tokensLocal.asStateFlow()

    private val _tokensError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val tokensError = _tokensError.asStateFlow()

    private val _tokensCount: MutableStateFlow<Int> = MutableStateFlow(3)
    val tokensCount = _tokensCount.asStateFlow()

    private val remoteTokensCount: MutableStateFlow<Int> = MutableStateFlow(3)

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

    private val _isSnackBarSuccess: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSnackBarSuccess = _isSnackBarSuccess.asStateFlow()

    private val _isDummyKeyAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isDummyKeyAvailable = _isDummyKeyAvailable.asStateFlow()

    private val _tokenManagement: MutableStateFlow<TokenManagement?> = MutableStateFlow(null)
    private val tokenManagement = _tokenManagement.asStateFlow()

    private val _suggestions: MutableStateFlow<MutableList<Suggestions>> =
        MutableStateFlow(mutableListOf())
    val suggestions = _suggestions.asStateFlow()

    init {
        getDatastoreModelEvent()
        observeOpenAI()
        prepareOpenAI()
        observeGPTList()
        prepareGPTList()
        fetchSignInState()
        getAllChatHistory()
        getLocalToken()
        resolveTokenCount()
        querySuggestions()
        observeSuggestionChanges()
    }

    fun getGPTResponse(gptQuery: String) {
        showPurchaseScreen.value = tokensError.value
        _isGPTResponseLoading.value = true

        if (tokensDepleted(gptQuery)) return

        viewModelScope.launch {
            try {
                isUpdate = chatList.value.isNotEmpty()

                val newQueryList = chatList.value.toMutableList()

                chatList.value = newQueryList.apply {
                    this.add(
                        AstraChatMessage(
                            role = ChatRole.User,
                            content = gptQuery,
                            isImagePrompt = false
                        )
                    )
                }

                checkOpenAiSyncAndGetResponse(newQueryList)

            } catch (e: Exception) {
                //Handle error scenario
                _isGPTResponseLoading.value = false
            }
        }
    }

    fun getImageUrlResponse(gptQuery: String) {
        _isGPTResponseLoading.value = true

        viewModelScope.launch {
            try {
                isUpdate = chatList.value.isNotEmpty()

                val newQueryList = chatList.value.toMutableList()

                chatList.value = newQueryList.apply {
                    this.add(
                        AstraChatMessage(
                            role = ChatRole.User,
                            content = gptQuery,
                            isImagePrompt = true
                        )
                    )
                }

                checkOpenAiSyncAndGetImageUrl(gptQuery)
            } catch (e: Exception) {
                //Handle error scenario
                _isGPTResponseLoading.value = false
            }
        }
    }

    fun getImageVariationResponse(fileName: String, inputStream: InputStream) {
        _isGPTResponseLoading.value = true

        viewModelScope.launch {
            try {
                isUpdate = chatList.value.isNotEmpty()

                checkOpenAiSyncAndGetImageVariation(fileName, inputStream)
            } catch (e: Exception) {
                //Handle error scenario
                _isGPTResponseLoading.value = false
            }
        }
    }


    private fun checkOpenAiSyncAndGetResponse(newQueryList: MutableList<AstraChatMessage>) {
        viewModelScope.launch {
            if (openAiAuth.value.isEmpty()) {
                delay(1000)
                checkOpenAiSyncAndGetResponse(newQueryList)
            } else {
                val chatMessageList = newQueryList.map {
                    ChatMessage(
                        it.role,
                        it.content
                    )
                }
                val response = repo.gtpChatResponse(
                    chatMessageList.toList(),
                    openAiAuth.value,
                    selectedGPTLLM.value?.llmVersion ?: "gpt-3.5-turbo"
                )

                val message = response.choices.first().message.content.orEmpty()
                val role = response.choices.first().message.role

                val newResponseList = chatList.value.toMutableList()
                chatList.value = newResponseList.apply {
                    this.add(
                        AstraChatMessage(
                            role = role,
                            content = message,
                            isImagePrompt = false
                        )
                    )
                }

                insertOrUpdateChatHistoryDb()
                updateLocalTokenAfterQuery()
                saveTokenManagement(
                    response.usage?.promptTokens,
                    response.usage?.completionTokens,
                    response.usage?.totalTokens
                )

                Log.d("GPT RESPONSE ID", response.id)
                _isGPTResponseLoading.value = false

            }
        }
    }

    private fun checkOpenAiSyncAndGetImageUrl(gptQuery: String) {
        viewModelScope.launch {
            if (openAiAuth.value.isEmpty()) {
                delay(1000)
                checkOpenAiSyncAndGetImageUrl(gptQuery)
            } else {
                try {
                    val imageCreation = ImageCreation(
                        prompt = gptQuery,
                        model = ModelId(selectedGPTLLM.value?.llmVersion ?: "dall-e-2"),
                        n = 1,
                        size = ImageSize.is1024x1024
                    )

                    val response = repo.gptImageCreation(
                        imageCreation,
                        openAiAuth.value
                    )

                    val url = response.firstOrNull()?.url

                    val loader = ImageLoader(getApplication<Application>().applicationContext)
                    val request = ImageRequest.Builder(getApplication<Application>().applicationContext)
                        .data(url)
                        .allowHardware(false)
                        .build()
                    val result =
                        (loader.execute(request) as SuccessResult).drawable
                    val bitmap = (result as BitmapDrawable).bitmap

                    val uri = bitmap.saveImageAndRetrieveUri(getApplication<Application>().applicationContext)

                    Log.i("FILE PATH", uri)

                    val newResponseList = chatList.value.toMutableList()
                    chatList.value = newResponseList.apply {
                        this.add(
                            AstraChatMessage(
                                role = ChatRole.Assistant,
                                content = uri,
                                isImagePrompt = true
                            )
                        )
                    }

                    insertOrUpdateChatHistoryDb()
                } catch (e: Exception) {
                    Log.e("Error", e.message.toString())
                }

                _isGPTResponseLoading.value = false
            }
        }
    }

    private fun checkOpenAiSyncAndGetImageVariation(fileName: String, inputStream: InputStream) {
        viewModelScope.launch {
            if (openAiAuth.value.isEmpty()) {
                delay(1000)
                checkOpenAiSyncAndGetImageVariation(fileName, inputStream)
            } else {
                try {
                    val imageVariation = ImageVariation(
                        image = FileSource(name = fileName, source = inputStream.source()),
                        model = ModelId("dall-e-2"),
                        n = 1,
                        size = ImageSize.is1024x1024
                    )

                    val response = repo.gptImageVariation(
                        imageVariation,
                        openAiAuth.value
                    )

                    val url = response.firstOrNull()?.url

                    val loader = ImageLoader(getApplication<Application>().applicationContext)
                    val request = ImageRequest.Builder(getApplication<Application>().applicationContext)
                        .data(url)
                        .allowHardware(false)
                        .build()
                    val result =
                        (loader.execute(request) as SuccessResult).drawable
                    val bitmap = (result as BitmapDrawable).bitmap

                    val uri = bitmap.saveImageAndRetrieveUri(getApplication<Application>().applicationContext)

                    val newResponseList = chatList.value.toMutableList()
                    chatList.value = newResponseList.apply {
                        this.add(
                            AstraChatMessage(
                                role = ChatRole.Assistant,
                                content = uri,
                                isImagePrompt = true
                            )
                        )
                    }

                    insertOrUpdateChatHistoryDb()
                } catch (e: Exception) {
                    Log.e("Error", e.message.toString())
                }
                _isGPTResponseLoading.value = false
            }
        }
    }


    private fun prepareOpenAI() {
        viewModelScope.launch {
            val openAi = repo.queryDataStore()

            openAi
                .catch { Log.e("OpenAI KEY", "Error querying key", it) }
                .collect {
                    Log.i("OpenAI KEY", "key ${it.openAi}")
                    if (isDummyKeyAvailable.value) return@collect
                    openAiAuth.value = it.openAi
                    remoteTokensCount.value = it.tokenCount
                    updateTokensFromRemote()
                }
        }
    }

    private fun prepareGPTList() {
        viewModelScope.launch {
            val list = mutableListOf<ChatGPTLLMs>()
            val gptVersion = repo.queryGPTLLMs()

            gptVersion
                .catch { Log.e("GPT LLM List", "Error querying GPT LLM List", it) }
                .collect {
                    Log.i("GPT LLM List", "llm ${it.llmVersion}")
                    list.add(it)
                }

            _gptLLMs.value.clear()
            _gptLLMs.value = list
            _selectedGPTLLM.value = gptLLMs.value.firstOrNull()
        }
    }

    private fun observeOpenAI() {
        viewModelScope.launch {
            val openAi = repo.observeDataStore()
            openAi
                .catch { Log.e("OpenAI KEY", "Error querying key", it) }
                .collect {
                    try {
                        Log.i("OpenAI KEY", "key ${it.items}")
                        if (isDummyKeyAvailable.value) return@collect
                        openAiAuth.value = it.items.first().openAi
                        remoteTokensCount.value = it.items.first().tokenCount
                        updateTokensFromRemote()
                    } catch (e: Exception) {
                        Log.e("OpenAI KEY", e.message.toString())
                    }
                }
        }
    }

    private fun observeGPTList() {
        viewModelScope.launch {
            val list = mutableListOf<ChatGPTLLMs>()
            val gptVersion = repo.observeGPTLLMs()

            gptVersion
                .catch { Log.e("GPT LLM List", "Error observing GPT LLM List", it) }
                .collect {
                    try {
                        Log.i("GPT LLM List", "key ${it.items}")
                        list.addAll(it.items)
                        _gptLLMs.value.clear()
                        _gptLLMs.value = list
                    } catch (e: Exception) {
                        Log.e("GPT LLM List", e.message.toString())
                    }
                }
        }
    }

    private fun updateTokensFromRemote() {
        if (tokensLocal.value.isNotEmpty()) {
            val tokenLocal = tokensLocal.value[0]
            if (tokensCount.value == 3) {
                val tokensUpdate = TokensUpdate(
                    tokenLocal.uid,
                    remoteTokensCount.value,
                    false,
                    DateUtil.currentDate()
                )
                updateLocalToken(tokensUpdate)
            }
        }
    }

    private fun tokensDepleted(query: String): Boolean {
        val remainingTokenCount = tokensLocal.value.firstOrNull()?.remainingCount ?: 0
        val tokenUnlimited = tokensLocal.value.firstOrNull()?.unlimited ?: false
        val tokensDepletedStatus = remainingTokenCount <= 0 && !tokenUnlimited
        if (tokensDepletedStatus) {
            _tokensError.value = true
            _isGPTResponseLoading.value = false
            showPurchaseScreen.value = tokensError.value
            getChatListWithError(query)
        }
        return tokensDepletedStatus
    }

    private fun insertOrUpdateChatHistoryDb() {
        val chatList = chatList.value
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
        if (tokensLocal.value.isEmpty()) {
            insertFreshToken()
        }
    }

    private fun insertFreshToken(unlimited: Boolean = false) {
        val token = com.bootsnip.aichat.db.Tokens(
            remainingCount = remoteTokensCount.value,
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
                    remoteTokensCount.value,
                    false,
                    DateUtil.currentDate()
                )
                updateLocalToken(tokensUpdate)
            }
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
        chatList.value = chatHistory?.chatMessageList?.toMutableList() ?: mutableListOf()
        currentRowId.value = uid.toLong()
    }

    fun fetchSignInState() {
        viewModelScope.launch {
            val response = repo.fetchAuthSession() as AWSCognitoAuthSession
            _isSignedIn.value = response.isSignedIn
            identity.value = response.identityIdResult.value
            if (!isSignedIn.value) {
                queryTokenManagement()
                observeTokenManagementChanges()
            }
            fetchCurrentUser()
            fetchUserAttributes()
        }
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            try {
                val response = repo.getCurrentUser()
                currentUserId.value = response.userId
                if (isSignedIn.value) {
                    queryTokenManagement()
                    observeTokenManagementChanges()
                }
            } catch (e: Exception) {
                Log.e("SignedOut", e.message.toString() + " ${currentUserId.value}")
            }
        }
    }

    private fun fetchUserAttributes() {
        viewModelScope.launch {
            try {
                val response = repo.fetchUserAttributes()
                _userName.value = response.find { it.key == AuthUserAttributeKey.email() }?.value

            } catch (e: Exception) {
                Log.e("SignedOut", e.message.toString() + " ${currentUserId.value}")
            }
        }
    }

    fun fetchChatHistoryWithAuthSession() {
        viewModelScope.launch {
            val response = repo.fetchAuthSession() as AWSCognitoAuthSession
            _isSignedIn.value = response.isSignedIn
            if (_isSignedIn.value) getChatHistoryWithCurrentUser()
            Log.i("IS SIGNED IN", _isSignedIn.value.toString())
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
        if (_isSignedIn.value) {
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
                    role = ChatRole(it.role),
                    content = it.content,
                    isImagePrompt = false
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
                        AstraChatMessage(
                            role = ChatRole.User,
                            content = gptQuery,
                            isImagePrompt = false
                        )
                    )
                    this.add(
                        AstraChatMessage(
                            role = ChatRole.Assistant,
                            content = "Oops, you've run out of tokens for today! Please purchase a subscription to continue enjoying Astra AI.",
                            isImagePrompt = false
                        )
                    )
                }
            }
        }
    }

    fun setSelectedLLM(llMVersion: String) {
        val selectLLM = gptLLMs.value.find { it.llmVersion == llMVersion }
        _selectedGPTLLM.value = selectLLM
        val isImagePrompt = selectLLM?.llmVersion?.startsWith("dall-e") ?: false
        _isImagePrompt.value = isImagePrompt
        startNewChat()
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

    private fun getDatastoreModelEvent() {
        viewModelScope.launch {
            val response = repo.dataStoreEventHub()
            response
                .collect {
                    val event = it.data as ModelSyncedEvent
                    if (event.model == "ChatGPTLLMs") {
                        if (event.isFullSync) {
                            _selectedGPTLLM.value = gptLLMs.value.firstOrNull()
                        }
                    }
                    if (event.model == "OpenAi") {
                        openAiSynced.value = event.isFullSync
                    }
                    Log.i("MODEL SYNCED", "Name of model synced is: ${event.model}")
                }
        }
    }

    fun signOut() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                when (repo.signOut()) {
                    is AWSCognitoAuthSignOutResult.CompleteSignOut -> {
                        fetchSignInState()
                        _isLoading.value = false
                        showSignOutSuccessSnackBar(true)
                    }

                    is AWSCognitoAuthSignOutResult.PartialSignOut -> {
                        fetchSignInState()
                        _isLoading.value = false
                        showSignOutSuccessSnackBar(true)
                    }

                    is AWSCognitoAuthSignOutResult.FailedSignOut -> {
                        _isLoading.value = false
                        showSignOutFailedSnackBar(true)
                    }
                }
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e("SignedOut", e.message.toString() + " ${currentUserId.value}")
            }
        }
    }

    private fun queryTokenManagement() {
        viewModelScope.launch {
            val response =
                repo.queryTokenManagement(currentUserId.value ?: "", identity.value ?: "")

            response
                .catch { Log.e("Queried Token Management", "Error querying tokens", it) }
                .collect {
                    try {
                        _tokenManagement.value = it
                        it.dummyKey.let { dummyKey ->
                            if (dummyKey.isNotEmpty()) openAiAuth.value = dummyKey
                            _isDummyKeyAvailable.value = dummyKey.isNotEmpty()
                        }
                    } catch (e: Exception) {
                        Log.e("Token Management", e.message.toString())
                    }
                }
        }
    }

    private fun observeTokenManagement() {
        viewModelScope.launch {
            val openAi =
                repo.observeTokenManagement(currentUserId.value ?: "", identity.value ?: "")
            openAi
                .catch { Log.e("Token Management", "Error querying key", it) }
                .collect {
                    try {
                        _tokenManagement.value = it.items.firstOrNull()
                        Log.i("Token Management", "key ${it.items}")
                        it.items.first().dummyKey.let { dummyKey ->
                            if (dummyKey.isNotEmpty()) openAiAuth.value = dummyKey
                            _isDummyKeyAvailable.value = dummyKey.isNotEmpty()
                        }

                    } catch (e: Exception) {
                        Log.e("Token Management", e.message.toString())
                    }
                }
        }

    }

    private fun observeTokenManagementChanges() {
        viewModelScope.launch {
            val openAi =
                repo.observeTokenManagementChanged(currentUserId.value ?: "", identity.value ?: "")
            openAi
                .catch { Log.e("Token Management", "Error querying key", it) }
                .collect {
                    try {
                        _tokenManagement.value = it.item()
                        Log.i("Token Management", "key ${it.item()}")
                        it.item().dummyKey.let { dummyKey ->
                            if (dummyKey.isNotEmpty()) openAiAuth.value = dummyKey
                            _isDummyKeyAvailable.value = dummyKey.isNotEmpty()
                        }

                    } catch (e: Exception) {
                        Log.e("Token Management", e.message.toString())
                    }
                }
        }

    }

    private fun saveTokenManagement(
        promptTokens: Int?,
        completionTokens: Int?,
        totalTokens: Int?
    ) {
        viewModelScope.launch {
            val remoteTokenIdentityId = tokenManagement.value?.identityId ?: ""
            val remoteTokenUserId = tokenManagement.value?.userId ?: ""

            val currentTokenPrompt = tokenManagement.value?.promptTokens ?: 0
            val currentTokenCompletion = tokenManagement.value?.completionTokens ?: 0
            val currentTokenTotal = tokenManagement.value?.totalTokens ?: 0

            val newPromptCount = currentTokenPrompt.plus((promptTokens ?: 0))
            val newCompletionCount = currentTokenCompletion.plus((completionTokens ?: 0))
            val newTotalCount = currentTokenTotal.plus((totalTokens ?: 0))

            val tokenManagementData = TokenManagement.builder()
                .identityId(identity.value ?: "")
                .unlimited(tokensLocal.value.firstOrNull()?.unlimited ?: false)
                .promptTokens(newPromptCount)
                .completionTokens(newCompletionCount)
                .totalTokens(newTotalCount)
                .userId(currentUserId.value ?: "")
                .build()

            if (remoteTokenUserId.isNotEmpty() || remoteTokenIdentityId.isNotEmpty()) {
                repo.updateTokenManagement(tokenManagementData)
            } else {
                repo.saveTokenManagement(tokenManagementData)
            }
        }
    }

    private fun querySuggestions() {
        viewModelScope.launch {
            val list = mutableListOf<Suggestions>()
            val response = repo.querySuggestions()

            response
                .catch { Log.e("Queried Suggestions", "Error querying suggestions", it) }
                .collect {
                    try {
                        list.add(it)
                    } catch (e: Exception) {
                        Log.e("Queried Suggestions", e.message.toString())
                    }
                }
            _suggestions.value.clear()
            _suggestions.value = list
        }
    }

    private fun observeSuggestionChanges() {
        viewModelScope.launch {
            val list = mutableListOf<Suggestions>()
            val response = repo.observeSuggestions()

            response
                .catch { Log.e("Queried Suggestions", "Error querying suggestions", it) }
                .collect {
                    try {
                        list.add(it.item())
                    } catch (e: Exception) {
                        Log.e("Token Management", e.message.toString())
                    }
                }
            _suggestions.value.clear()
            _suggestions.value = list
        }

    }

    fun showSignInSuccessSnackBar(showSuccess: Boolean) {
        _showSignInSuccessSnackBar.value = showSuccess
        _isSnackBarSuccess.value = true
    }

    fun showSignOutSuccessSnackBar(showSuccess: Boolean) {
        _showSignOutSuccessSnackBar.value = showSuccess
        _isSnackBarSuccess.value = true
    }

    fun showSignOutFailedSnackBar(showFailed: Boolean) {
        _showSignOutFailedSnackBar.value = showFailed
        _isSnackBarSuccess.value = false
    }

    fun showGPTResponseLoadingIndicator() {
        _isGPTResponseLoading.value = true
    }

    fun dismissGPTResponseLoadingIndicator() {
        _isGPTResponseLoading.value = false
    }
}