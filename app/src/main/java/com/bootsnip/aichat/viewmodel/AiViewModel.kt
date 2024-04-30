package com.bootsnip.aichat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bootsnip.aichat.model.Quote
import com.bootsnip.aichat.repo.IAiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiViewModel @Inject constructor(
    private val repo: IAiRepo,
    application: Application
) : AndroidViewModel(application) {

    private val _items: MutableStateFlow<List<Quote>> = MutableStateFlow(emptyList())
    val items: StateFlow<List<Quote>> = _items.asStateFlow()

    suspend fun loadItems() {
        viewModelScope.launch {
            repo.getQuotes().onSuccess { quotesResult ->
                _items.update {
                    quotesResult.results
                }
            }.onFailure {
                Log.e("Error", it.message ?: "unexpected error")

            }
        }
    }
}