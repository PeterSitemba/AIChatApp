package com.bootsnip.aichat.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bootsnip.aichat.ui.theme.DarkGrey
import com.bootsnip.aichat.ui.theme.Purple80

@Composable
fun PurchaseSubscriptionScreen(
) {

    var isYearlyOptionSelected by remember {
        mutableStateOf(true)
    }

    var isWeeklyOptionSelected by remember {
        mutableStateOf(false)
    }

    val borderStroke = BorderStroke(
        width = 2.dp,
        color = Purple80
    )

    val buttonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = DarkGrey,
        contentColor = Color.Unspecified,
        disabledContainerColor = Color.Unspecified,
        disabledContentColor = Color.Unspecified
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            Text(
                text = "Unlock Pro Features",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
                fontSize = 32.sp
            )

            Surface(
                border = BorderStroke(1.dp, Color.Gray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 22.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(text = "Free trial enabled")

                    Switch(checked = isWeeklyOptionSelected, onCheckedChange = {
                        if (it) {
                            isYearlyOptionSelected = false
                            isWeeklyOptionSelected = true
                        } else {
                            isWeeklyOptionSelected = false
                            isYearlyOptionSelected = true
                        }
                    })
                }
            }

            Box(modifier = Modifier.fillMaxWidth()) {

                OutlinedButton(
                    onClick = {
                        isYearlyOptionSelected = true
                        isWeeklyOptionSelected = false
                    },
                    colors = if (isYearlyOptionSelected) buttonColors else ButtonDefaults.outlinedButtonColors(),
                    border = if (isYearlyOptionSelected) borderStroke else ButtonDefaults.outlinedButtonBorder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 22.dp),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(
                        vertical = 8.dp,
                        horizontal = 12.dp
                    )
                ) {

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(text = "YEARLY ACCESS")
                            Text(text = "Just KShs 7500 per year")
                        }

                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(text = "or just")
                            Text(text = "KShs 144 per week")
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(y = (-14).dp)
                        .padding(end = 8.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = Purple80,
                ) {
                    Text(
                        text = "Save 85%!!",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

            }


            OutlinedButton(
                onClick = {
                    isYearlyOptionSelected = false
                    isWeeklyOptionSelected = true
                },
                colors = if (isWeeklyOptionSelected) buttonColors else ButtonDefaults.outlinedButtonColors(),
                border = if (isWeeklyOptionSelected) borderStroke else ButtonDefaults.outlinedButtonBorder,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 22.dp),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = 12.dp
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "3 DAY FREE TRIAL")

                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "then KShs 900")
                        Text(text = "per week")
                    }
                }
            }
        }

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(text = "Continue", fontSize = 20.sp, modifier = Modifier.padding(vertical = 8.dp))
        }

    }

}