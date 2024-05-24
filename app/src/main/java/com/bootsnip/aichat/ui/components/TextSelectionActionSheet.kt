package com.bootsnip.aichat.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bootsnip.aichat.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AstraTextSelectionActionSheet(
    showRegenerate: Boolean = false,
    showShareEntireChat: Boolean = false,
    onDismissSheet: () -> Unit,
    onCopyClicked: () -> Unit,
    onSelectTextClicked: () -> Unit,
    onRegenerateResponseClicked: () -> Unit = {},
    onShareEntireChatClicked: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = onDismissSheet
    ) {
        ListItem(
            headlineContent = { Text("Copy") },
            leadingContent = { Icon(painterResource(id = R.drawable.content_copy), null) },
            modifier = Modifier.clickable {
                onCopyClicked()
                onDismissSheet()
            }
        )

        ListItem(
            headlineContent = { Text("Select text") },
            leadingContent = { Icon(painterResource(id = R.drawable.text_box_outline), null) },
            modifier = Modifier
                .clickable {
                    onSelectTextClicked()
                    onDismissSheet()
                }
                .padding(bottom = if (showRegenerate) 0.dp else 32.dp)
        )

        if (showShareEntireChat) {
            ListItem(
                headlineContent = { Text("Share entire chat") },
                leadingContent = { Icon(Icons.Default.Share, null) },
                modifier = Modifier
                    .clickable {
                        onShareEntireChatClicked()
                        onDismissSheet()
                    }
            )
        }

        if (showRegenerate) {
            ListItem(
                headlineContent = { Text("Regenerate response") },
                leadingContent = { Icon(Icons.Default.Refresh, null) },
                modifier = Modifier
                    .clickable {
                        onRegenerateResponseClicked()
                        onDismissSheet()
                    }
                    .padding(bottom = 32.dp)
            )
        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun UserTextSelectionActionSheet(
    onDismissSheet: () -> Unit,
    onCopyClicked: () -> Unit,
    onSelectTextClicked: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissSheet
    ) {
        ListItem(
            headlineContent = { Text("Copy") },
            leadingContent = { Icon(painterResource(id = R.drawable.content_copy), null) },
            modifier = Modifier.clickable {
                onCopyClicked()
                onDismissSheet()
            }
        )

        ListItem(
            headlineContent = { Text("Select text") },
            leadingContent = { Icon(painterResource(id = R.drawable.text_box_outline), null) },
            modifier = Modifier
                .clickable {
                    onSelectTextClicked()
                    onDismissSheet()
                }
                .padding(bottom = 24.dp)
        )
    }
}