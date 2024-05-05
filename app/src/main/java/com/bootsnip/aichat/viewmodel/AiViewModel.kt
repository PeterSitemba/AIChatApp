package com.bootsnip.aichat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.bootsnip.aichat.db.ChatHistory
import com.bootsnip.aichat.db.ChatHistoryUpdateFav
import com.bootsnip.aichat.repo.IAiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiViewModel @Inject constructor(
    private val repo: IAiRepo,
    application: Application
) : AndroidViewModel(application) {

    val chatList: MutableStateFlow<MutableList<ChatMessage>> = MutableStateFlow(mutableListOf())

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _chatHistory: MutableStateFlow<List<ChatHistory>> = MutableStateFlow(mutableListOf())
    val chatHistory = _chatHistory.asStateFlow()

    private val _favChatHistory: MutableStateFlow<List<ChatHistory>> = MutableStateFlow(mutableListOf())
    val favChatHistory = _favChatHistory.asStateFlow()

    fun getGPTResponse(gptQuery: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val newQueryList = chatList.value.toMutableList()
                chatList.value = newQueryList.apply {
                    this.add(
                        ChatMessage(ChatRole.User, gptQuery)
                    )
                }

                val response = repo.gtpChatResponse(newQueryList.toList())
                val message = response.choices.first().message.content.orEmpty()
                val role = response.choices.first().message.role


                val newResponseList = chatList.value.toMutableList()
                chatList.value = newResponseList.apply {
                    this.add(
                        ChatMessage(role, message)
                    )
                }

                Log.d("GPT RESPONSE ID", response.id)
                _isLoading.value = false

            } catch (e: Exception) {
                //Handle error scenario
                _isLoading.value = false
            }
        }
    }

    fun insertChatHistory(chatHistory: ChatHistory){
        viewModelScope.launch {
            repo.insertChatHistory(chatHistory)
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
}