package com.bootsnip.aichat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.bootsnip.aichat.R
import com.bootsnip.aichat.ui.theme.DarkRed

@Composable
fun SignOutDialog(
    showSignOutDialog: (Boolean) -> Unit,
    signOutClicked: () -> Unit
) {

    val buttonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = DarkRed,
        contentColor = Color.White,
        disabledContainerColor = Color.Unspecified,
        disabledContentColor = Color.Unspecified
    )

    Dialog(onDismissRequest = { showSignOutDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                Icon(
                    Icons.AutoMirrored.Default.ExitToApp,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                        .size(36.dp),
                    tint = DarkRed
                )

                Text(
                    text = stringResource(id = R.string.sign_out),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top = 16.dp)
                )

                Text(
                    text = stringResource(id = R.string.sign_out_body),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Row(Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            signOutClicked()
                            showSignOutDialog(false)
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 24.dp).weight(0.5f),
                        shape = RoundedCornerShape(10.dp),
                        colors = buttonColors
                    ) {
                        Text(
                            text = stringResource(id = R.string.sign_out),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            showSignOutDialog(false)
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 24.dp).weight(0.5f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                }
            }
        }
    }

}