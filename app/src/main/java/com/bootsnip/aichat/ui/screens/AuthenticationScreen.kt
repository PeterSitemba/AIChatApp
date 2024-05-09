package com.bootsnip.aichat.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.amplifyframework.ui.authenticator.SignedInState
import com.amplifyframework.ui.authenticator.ui.Authenticator
import kotlinx.coroutines.launch

@Composable
fun AuthenticationScreen() {
    Box(Modifier.fillMaxSize()) {
        Authenticator(
            modifier = Modifier.align(Alignment.Center)
        ) {
            SignedInContent(it)
        }
    }
}

@Composable
fun SignedInContent(state: SignedInState) {
    val scope = rememberCoroutineScope()
    Column {
        Text("You've signed in as ${state.user.username}")
        Button(onClick = { scope.launch { state.signOut() } }) {
            Text("Sign Out")
        }
    }
}