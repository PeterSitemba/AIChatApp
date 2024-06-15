package com.bootsnip.aichat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {

    private val _showSplash = MutableStateFlow(true)
    val showSplash = _showSplash.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1500L)
            _showSplash.value = false
        }
    }
}