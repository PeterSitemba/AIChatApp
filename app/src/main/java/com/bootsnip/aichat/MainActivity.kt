package com.bootsnip.aichat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bootsnip.aichat.navigation.AstraNavGraph
import com.bootsnip.aichat.ui.theme.AIChatTheme
import com.bootsnip.aichat.ui.theme.backgroundColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       /* enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(backgroundColor),
        )*/

        setContent {
            AIChatTheme {
                AstraNavGraph()
            }
        }
    }
}