package com.bootsnip.aichat.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.amplifyframework.datastore.generated.model.ChatGPTLLMs
import com.bootsnip.aichat.R

@Composable
fun GPTDropDownList(
    llmList: List<ChatGPTLLMs>,
    expanded: Boolean,
    unlimited: Boolean,
    showDropDown: (Boolean) -> Unit,
    selectedLLM: (String, Boolean) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { showDropDown(false) },
        offset = DpOffset((-12).dp, 0.dp)
    ) {
        llmList.forEach {
            DropdownMenuItem(
                text = {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            it.displayName,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (it.subscribed && !unlimited) {
                            Icon(
                                painter = painterResource(id = R.drawable.professional_hexagon),
                                contentDescription = "",
                                modifier = Modifier.align(Alignment.TopEnd)
                            )
                        }
                    }
                },
                onClick = {
                    selectedLLM(it.llmVersion, it.subscribed)
                    showDropDown(false)
                },
                contentPadding = PaddingValues(
                    top = 0.dp,
                    bottom = 0.dp,
                    start = 0.dp,
                    end = 0.dp
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            HorizontalDivider()
        }
    }
}