package com.bootsnip.aichat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bootsnip.aichat.navigation.AllDestinations
import com.bootsnip.aichat.R

@Composable
fun AppDrawer(
    route: String,
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit = {},
    navigateToChatHistory: () -> Unit = {},
    navigateToAuthentication: () -> Unit = {},
    navigateToSubscription: () -> Unit = {},
    closeDrawer: () -> Unit = {}
) {
    ModalDrawerSheet(modifier = Modifier.requiredWidth(250.dp)) {
        DrawerHeader(modifier)
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.spacer_padding)))
        NavigationDrawerItem(
            label = {
                Text(
                    text = "Home",
                    style = MaterialTheme.typography.labelSmall
                )
            },
            selected = route == AllDestinations.HOME,
            onClick = {
                navigateToHome()
                closeDrawer()
            },
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
            shape = MaterialTheme.shapes.small
        )

        NavigationDrawerItem(
            label = { Text(text = "Chat History", style = MaterialTheme.typography.labelSmall) },
            selected = route == AllDestinations.CHAT_HISTORY,
            onClick = {
                navigateToChatHistory()
            },
            icon = { Icon(painterResource(id = R.drawable.chat), contentDescription = null) },
            shape = MaterialTheme.shapes.small
        )

        NavigationDrawerItem(
            label = { Text(text = "Sign In", style = MaterialTheme.typography.labelSmall) },
            selected = route == AllDestinations.AUTHENTICATION,
            onClick = {
                navigateToAuthentication()
                closeDrawer()
            },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            shape = MaterialTheme.shapes.small
        )

        NavigationDrawerItem(
            label = { Text(text = "Unlock Pro Features", style = MaterialTheme.typography.labelSmall) },
            selected = route == AllDestinations.SUBSCRIPTION,
            onClick = {
                navigateToSubscription()
                closeDrawer()
            },
            icon = { Icon(painterResource(id = R.drawable.professional_hexagon), contentDescription = null) },
            shape = MaterialTheme.shapes.small
        )
    }
}


@Composable
fun DrawerHeader(modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary)
            .padding(dimensionResource(id = R.dimen.header_padding))
            .fillMaxWidth()
    ) {

        Image(
            painterResource(id = R.drawable.user_avatar),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(dimensionResource(id = R.dimen.header_image_size))
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.spacer_padding)))

        Text(
            text = stringResource(id = R.string.app_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Preview
@Composable
fun DrawerHeaderPreview() {
    AppDrawer(modifier = Modifier, route = AllDestinations.HOME)
}