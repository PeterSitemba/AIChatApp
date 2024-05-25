package com.bootsnip.aichat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bootsnip.aichat.R
import com.bootsnip.aichat.navigation.AllDestinations

@Composable
fun AppDrawer(
    route: String,
    unlimited: Boolean,
    isSignedIn: Boolean,
    userName: String,
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit = {},
    navigateToChatHistory: () -> Unit = {},
    navigateToAuthentication: () -> Unit = {},
    navigateToSubscription: () -> Unit = {},
    signOutClicked: () -> Unit = {},
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

        if (!unlimited) {
            NavigationDrawerItem(
                label = {
                    Text(
                        text = "Unlock Pro Features",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = route == AllDestinations.SUBSCRIPTION,
                onClick = {
                    navigateToSubscription()
                    closeDrawer()
                },
                icon = {
                    Icon(
                        painterResource(id = R.drawable.professional_hexagon),
                        contentDescription = null
                    )
                },
                shape = MaterialTheme.shapes.small
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        HorizontalDivider(Modifier.padding(horizontal = 16.dp))

        NavigationDrawerItem(
            label = {
                Text(text = if (isSignedIn) userName.ifEmpty { "User" } else "Sign In",
                    style = MaterialTheme.typography.labelSmall)
            },
            selected = route == AllDestinations.AUTHENTICATION,
            onClick = {
                if (isSignedIn) {
                    signOutClicked()
                } else {
                    navigateToAuthentication()
                }
                closeDrawer()
            },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            badge = {
                if (isSignedIn) Icon(
                    Icons.AutoMirrored.Default.ExitToApp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}


@Composable
fun DrawerHeader(modifier: Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 32.dp,
                bottom = 16.dp
            )
            .fillMaxWidth()
    ) {

        Image(
            painterResource(id = R.drawable.logo),
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
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun DrawerHeaderPreview() {
    AppDrawer(
        modifier = Modifier,
        route = AllDestinations.HOME,
        unlimited = true,
        isSignedIn = false,
        userName = ""
    )
}