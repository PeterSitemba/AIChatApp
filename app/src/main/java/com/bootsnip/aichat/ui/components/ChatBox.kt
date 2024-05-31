package com.bootsnip.aichat.ui.components

import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.bootsnip.aichat.R
import com.bootsnip.aichat.util.shareImage
import kotlinx.coroutines.launch

@Composable
fun AiChatBox(
    response: String = "",
    isImagePrompt: Boolean = false,
    modifier: Modifier
) {

    val context = LocalContext.current

    val iconButtonColors = IconButtonColors(
        containerColor = Color.Gray,
        contentColor = Color.White,
        disabledContainerColor = Color.Unspecified,
        disabledContentColor = Color.Unspecified
    )

    val coroutineScope = rememberCoroutineScope()

    var sharingLoadingIndicator by remember {
        mutableStateOf(false)
    }

    var imageLoadingIndicator by remember {
        mutableStateOf(true)
    }

    Row(modifier.fillMaxWidth()) {
        AiAvatar()
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(text = stringResource(id = R.string.app_name))
            if (isImagePrompt) {
                Box {
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 12.dp, end = 8.dp)
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(20.dp)),
                        model = response,
                        loading = {
                            DotsLoadingIndicator()
                            imageLoadingIndicator = true
                        },
                        onSuccess = {
                            imageLoadingIndicator = false
                        },
                        onError = {
                            imageLoadingIndicator = false
                        },
                        contentDescription = null,
                    )
                    if(!imageLoadingIndicator){
                        IconButton(
                            onClick = {
                                sharingLoadingIndicator = true
                                coroutineScope.launch {
                                    val loader = ImageLoader(context)
                                    val request = ImageRequest.Builder(context)
                                        .data(response)
                                        .allowHardware(false)
                                        .build()
                                    val result = (loader.execute(request) as SuccessResult).drawable
                                    val bitmap = (result as BitmapDrawable).bitmap
                                    bitmap.shareImage(context) {
                                        sharingLoadingIndicator = it
                                    }
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 20.dp, end = 12.dp),
                            colors = iconButtonColors
                        ) {
                            if (sharingLoadingIndicator)
                                CircularProgressIndicator(
                                    modifier = Modifier.padding(4.dp)
                                )
                            else
                                Icon(
                                    painterResource(id = R.drawable.arrow_down),
                                    contentDescription = ""
                                )
                        }

                    }
                }
            } else {
                Text(text = response, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun UserChatBox(
    query: String = "",
    modifier: Modifier
) {
    Row(modifier.fillMaxWidth()) {
        UserAvatar()
        Spacer(modifier = Modifier.width(5.dp))
        Column {
            Text(text = stringResource(R.string.you))
            Text(text = query, modifier = Modifier.fillMaxWidth())
        }
    }
}