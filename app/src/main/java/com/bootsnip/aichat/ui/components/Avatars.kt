package com.bootsnip.aichat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bootsnip.aichat.R

@Composable
fun AiAvatar() {
    Image(
        painter = painterResource(id = R.drawable.astra_ai_logo),
        contentDescription = "ai_avatar",
        modifier = Modifier
            .clip(CircleShape)
            .size(20.dp)
    )
}

@Composable
fun UserAvatar() {
    Image(
        painter = painterResource(id = R.drawable.user_avatar),
        contentDescription = "user_avatar",
        modifier = Modifier
            .clip(CircleShape)
            .size(20.dp)
    )
}