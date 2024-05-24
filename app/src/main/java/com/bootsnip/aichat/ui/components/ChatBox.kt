package com.bootsnip.aichat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AiChatBox(
    response: String = "",
    modifier: Modifier
) {
    Row(modifier.fillMaxWidth()) {
        AiAvatar()
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(text = "Astra AI")
            Text(text = response, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun UserChatBox(
    query: String = "",
    modifier: Modifier
) {
    Row(modifier.fillMaxWidth()) {
        UserAvatar()
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(text = "You")
            Text(text = query, modifier = Modifier.fillMaxWidth())
        }
    }
}