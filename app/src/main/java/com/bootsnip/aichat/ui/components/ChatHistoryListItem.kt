package com.bootsnip.aichat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.bootsnip.aichat.R
import com.bootsnip.aichat.db.ChatHistoryUpdateFav
import com.bootsnip.aichat.ui.theme.Orange
import com.bootsnip.aichat.viewmodel.AiViewModel

@Composable
fun ChatHistoryListItem(
    placeHolder: String,
    id: Int,
    isFav: Boolean = false,
    viewModel: AiViewModel = hiltViewModel()
) {

    ConstraintLayout(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
    ) {

        val (profilePic, chat, favIcon) = createRefs()

        Image(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(45.dp)
                .constrainAs(profilePic) {
                    start.linkTo(parent.start)
                },
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo"
        )

        Text(
            text = placeHolder,
            maxLines = 2,
            fontSize = 14.sp,
            modifier = Modifier.constrainAs(chat) {
                start.linkTo(profilePic.end)
                end.linkTo(favIcon.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            }
        )

        Icon(
            modifier = Modifier
                .constrainAs(favIcon) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(start = 8.dp)
                .clickable {
                    viewModel.updateChatHistoryStatus(
                        ChatHistoryUpdateFav(
                            uid = id,
                            fav = if(isFav) 0 else 1
                        )
                    )
                },
            imageVector = Icons.Default.Star,
            contentDescription = "",
            tint = if(isFav) Orange else Color.White
        )
    }
}