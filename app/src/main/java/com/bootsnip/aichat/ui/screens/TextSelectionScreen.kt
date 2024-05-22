package com.bootsnip.aichat.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bootsnip.aichat.viewmodel.AstraViewModel

@Composable
fun TextSelectionScreen(
    viewModel: AstraViewModel
) {

    val selectedResponse = viewModel.selectedResponse.collectAsStateWithLifecycle().value
    val selectedQuery = viewModel.selectedQuery.collectAsStateWithLifecycle().value
    val isResponseSelected = viewModel.isResponseSelected.collectAsStateWithLifecycle().value
    val isQuerySelected = viewModel.isQuerySelected.collectAsStateWithLifecycle().value
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        SelectionContainer(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(text = if (isQuerySelected) selectedQuery else if (isResponseSelected) selectedResponse else "")
        }
    }

}