package com.bootsnip.aichat.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun EmptyScreenPlaceholder(
    textPlaceHolder: String
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = textPlaceHolder,
            fontSize = 24.sp,
            modifier = Modifier.fillMaxWidth().align(Alignment.Center),
            textAlign = TextAlign.Center,
            style = TextStyle(
                color = Color.Gray,
                fontStyle = FontStyle.Italic
            )
        )
    }
}