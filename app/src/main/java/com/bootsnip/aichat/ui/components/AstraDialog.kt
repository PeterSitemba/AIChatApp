package com.bootsnip.aichat.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.bootsnip.aichat.ui.components.Suggestions.getRandomSuggestions
import com.bootsnip.aichat.ui.components.Suggestions.suggestionList

@Composable
fun AstraSuggestionsDialog(
    showSuggestionDialog: (Boolean) -> Unit,
    onSuggestionClicked: (String) -> Unit
) {
    Dialog(onDismissRequest = { showSuggestionDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                items(getRandomSuggestions(suggestionList)) {
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
        "Remind me to call my mom tomorrow afternoon.",
        "Tell me a joke to lighten up my mood.",
        "What's the latest news on advancements in renewable energy?",
        "Set up a reminder for my dentist appointment next week.",
        "Recommend a good sci-fi movie released in the last year.",
        "What's the weather forecast for this weekend in New York City?",
        "Create a shopping list for a Mexican-themed dinner party.",
        "Find a beginner's guide to meditation.",
        "Can you summarize the key points from the article I was reading earlier?",
        "Translate this phrase into French: 'Where is the nearest train station?'",
        "Play some upbeat music to help me focus while I work.",
        "What are some easy DIY home organization tips?",
        "Check my calendar for any upcoming birthdays or anniversaries.",
        "Find a TED Talk on the importance of emotional intelligence.",
        "Remind me to pick up dry cleaning on my way home from work.",
        "Suggest a 20-minute workout routine I can do at home with no equipment.",
        "What are the top tourist attractions in Tokyo, Japan?",
        "Set a timer for 45 minutes for a pomodoro session.",
        "Order a pizza for delivery, pepperoni and mushrooms.",
        "Find a reputable mechanic near me for an oil change.",
        "What are some healthy snack options I can munch on during work?",
        "Remind me to water the plants every other day.",
        "Look up reviews for the new Italian restaurant downtown.",
        "Tell me a fun fact about space exploration.",
        "What's the best way to clean stainless steel appliances?",
        "Book a table for two at a romantic restaurant for Friday night.",
        "Give me a summary of the latest best-selling novel in the mystery genre.",
        "Find a tutorial on how to tie a tie.",
        "What are the current COVID-19 guidelines for traveling to Spain?"
    )

    fun getRandomSuggestions(suggestions: List<String>): List<String> {
        if (suggestions.size <= 7) return suggestions
        val shuffledSuggestions = suggestions.shuffled()
        return shuffledSuggestions.take(7)
    }
}
