package com.bootsnip.aichat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bootsnip.aichat.R

@Composable
fun HomePlaceholder(
    poweredBy: String,
    modifier: Modifier,
    suggestionButtonCallback: () -> Unit
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            modifier = Modifier
                .size(70.dp)
                .padding(bottom = 8.dp),
            contentDescription = ""
        )

        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp
        )
        Row(modifier = Modifier.padding(bottom = 8.dp)) {
            Text(
                text = stringResource(id = R.string.powered_by),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = poweredBy,
                fontSize = 12.sp
            )
        }

        OutlinedButton(onClick = suggestionButtonCallback) {
            Row {
                Text(text = stringResource(id = R.string.suggestions))
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.creation),
                    modifier = Modifier
                        .size(20.dp),
                    contentDescription = ""
                )
            }

        }
    }

}