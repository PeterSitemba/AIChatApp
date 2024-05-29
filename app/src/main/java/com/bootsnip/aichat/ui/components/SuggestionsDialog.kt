package com.bootsnip.aichat.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bootsnip.aichat.R
import com.bootsnip.aichat.ui.components.Suggestions.getRandomSuggestions
import com.bootsnip.aichat.ui.components.Suggestions.suggestionList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SuggestionsDialog(
    showSuggestionDialog: (Boolean) -> Unit,
    onSuggestionClicked: (String) -> Unit,
    suggestions: List<String>
) {
    Dialog(onDismissRequest = { showSuggestionDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {

                stickyHeader {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.creation),
                            modifier = Modifier
                                .size(32.dp),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                items(
                    if (suggestions.isNotEmpty())
                        getRandomSuggestions(suggestions)
                    else
                        getRandomSuggestions(suggestionList)
                ) {
                    SuggestionListItem(it) {
                        onSuggestionClicked(it)
                        showSuggestionDialog(false)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun SuggestionListItem(
    suggestion: String,
    onSuggestionClicked: () -> Unit
) {
    OutlinedButton(
        onClick = onSuggestionClicked,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonColors(
            contentColor = Color.LightGray,
            containerColor = Color.Unspecified,
            disabledContentColor = Color.Unspecified,
            disabledContainerColor = Color.Unspecified
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = suggestion,
            textAlign = TextAlign.Center
        )
    }
}

object Suggestions {
    val suggestionList = listOf(
        "Find me a recipe for a vegetarian lasagna with a twist.",
        "Tell me a joke to lighten up my mood.",
        "What's the latest news on advancements in renewable energy?",
        "Create a shopping list for a Mexican-themed dinner party.",
        "Find a beginner's guide to meditation.",
        "Translate this phrase into French: 'Where is the nearest train station?'",
        "What are some easy DIY home organization tips?",
        "Suggest a 20-minute workout routine I can do at home with no equipment.",
        "What are the top tourist attractions in Tokyo, Japan?",
        "Find a reputable mechanic near me for an oil change.",
        "What are some healthy snack options I can munch on during work?",
        "Tell me a fun fact about space exploration.",
        "What's the best way to clean stainless steel appliances?",
        "Find a tutorial on how to tie a tie."
    )

    fun getRandomSuggestions(suggestions: List<String>): List<String> {
        if (suggestions.size <= 7) return suggestions
        val shuffledSuggestions = suggestions.shuffled()
        return shuffledSuggestions.take(7)
    }
}
