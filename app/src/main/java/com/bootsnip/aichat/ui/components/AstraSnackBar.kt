package com.bootsnip.aichat.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bootsnip.aichat.ui.theme.DarkGreen
import com.bootsnip.aichat.ui.theme.DarkRed

@Composable
fun AstraSnackBar(
    message: String,
    success: Boolean
) {
    Snackbar(
        containerColor = if (success) DarkGreen else DarkRed,
        contentColor = Color.White,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Text(message)
    }
}
