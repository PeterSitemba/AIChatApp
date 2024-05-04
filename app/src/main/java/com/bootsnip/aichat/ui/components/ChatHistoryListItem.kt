package com.bootsnip.aichat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bootsnip.aichat.R

@Composable
fun ChatHistoryListItem(
    placeHolder: String
) {
    Row {
        Image(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo"
        )

        Text(
            placeHolder
        )
    }
}