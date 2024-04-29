package com.bootsnip.aichat.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.bootsnip.aichat.ui.components.AiChatBox
import com.bootsnip.aichat.ui.components.UserChatBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    ConstraintLayout(Modifier.fillMaxSize()) {
        val (chatArea, inputField) = createRefs()
        var prompt by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .constrainAs(chatArea) {
                    linkTo(parent.top, inputField.top, topMargin = 16.dp, bottomMargin = 20.dp, bias = 0F)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    height = Dimension.preferredWrapContent
                }
                .verticalScroll(rememberScrollState())
        ) {
            UserChatBox()
            Spacer(modifier = Modifier.height(10.dp))
            AiChatBox()
            Spacer(modifier = Modifier.height(10.dp))
            UserChatBox()
            Spacer(modifier = Modifier.height(10.dp))
            AiChatBox()
            Spacer(modifier = Modifier.height(10.dp))
            UserChatBox()
            Spacer(modifier = Modifier.height(10.dp))
            AiChatBox()
        }

        TextField(
            modifier = Modifier
                .constrainAs(inputField) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                }
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            value = prompt,
            onValueChange = { prompt = it },
            placeholder = { Text("Message") },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

    }
}