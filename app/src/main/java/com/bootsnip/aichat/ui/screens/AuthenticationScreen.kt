package com.bootsnip.aichat.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.amplifyframework.ui.authenticator.ui.Authenticator
import com.bootsnip.aichat.viewmodel.AstraViewModel

@Composable
fun AuthenticationScreen(
    viewModel: AstraViewModel,
    navHostController: NavHostController
) {
    Box(Modifier.fillMaxSize()) {
        Authenticator(
            modifier = Modifier.align(Alignment.Center)
        ) {
            LaunchedEffect(Unit) {
                viewModel.fetchSignInState()
                viewModel.showSignInSuccessSnackBar(true)
                navHostController.popBackStack()
            }
        }
    }
}