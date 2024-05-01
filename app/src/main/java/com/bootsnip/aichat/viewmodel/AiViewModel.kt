package com.bootsnip.aichat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bootsnip.aichat.model.ChatMessageData
import com.bootsnip.aichat.repo.IAiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiViewModel @Inject constructor(
    private val repo: IAiRepo,
    application: Application
) : AndroidViewModel(application) {

    val chatList: MutableStateFlow<MutableList<ChatMessageData>> = MutableStateFlow(mutableListOf())

    fun getGPTResponse(gtpQuery: String) {
        viewModelScope.launch {
            try {
                val response = repo.gtpChatResponse(gtpQuery)
                val message = response.choices.first().message.content.orEmpty()
                val role = response.choices.first().message.role

                val newList = chatList.value.toMutableList()
                chatList.value = newList.apply {
                    this.add(
                        ChatMessageData(role, message)
                    )
                }.toMutableList()

                Log.d("GPT RESPONSE", message)

            } catch (e: Exception) {
                //Handle error scenario
            }
        }
    }
}