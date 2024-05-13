package com.bootsnip.aichat.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bootsnip.aichat.navigation.CLOSE_DRAWER
import com.bootsnip.aichat.navigation.ChatHistoryNavGraph
import com.bootsnip.aichat.navigation.UID
import com.bootsnip.aichat.ui.theme.AIChatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatHistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AIChatTheme {
                ChatHistoryNavGraph { closeDrawer, uid ->
                    navigateBackWithResult(closeDrawer, uid)
                }
            }
        }
    }

    private fun navigateBackWithResult(closeDrawer: Boolean, uid: String) {
        if (closeDrawer) {
            val intent = Intent(
                this,
                ChatHistoryActivity::class.java
            )
            intent.putExtra(CLOSE_DRAWER, true)
            intent.putExtra(UID, uid)
            setResult(RESULT_OK, intent)
        }
        finish()
    }
}
