package com.bootsnip.aichat.util

import android.graphics.Color
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
@ReadOnlyComposable
fun textResource(@StringRes id: Int): CharSequence =
    LocalContext.current.resources.getText(id)

// Creating a function to display the styled text
@Composable
fun StyledText(text: CharSequence, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context) },
        update = {
            it.text = text
            it.setTextColor(Color.WHITE)
            it.textSize = 14F
        }
    )
}