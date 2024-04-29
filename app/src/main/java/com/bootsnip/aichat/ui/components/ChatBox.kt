package com.bootsnip.aichat.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bootsnip.aichat.R

@Composable
fun AiChatBox(
    @StringRes response: Int = R.string.gpt_response
) {
    Row {
        AiAvatar()
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(text = "AI APP")
            Text(text = stringResource(id = response))
        }
    }
}

@Composable
fun UserChatBox(
    @StringRes query: Int = R.string.user_query
) {
    Row {
        UserAvatar()
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(text = "You")
            Text(text = stringResource(id = query))
        }
    }
}