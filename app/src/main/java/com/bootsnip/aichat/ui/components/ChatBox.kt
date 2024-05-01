package com.bootsnip.aichat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AiChatBox(
    response: String = ""
) {
    Row {
        AiAvatar()
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(text = "AI APP")
            Text(text = response)
        }
    }
}

@Composable
fun UserChatBox(
    query: String = ""
) {
    Row {
        UserAvatar()
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(text = "You")
            Text(text = query)
        }
    }
}