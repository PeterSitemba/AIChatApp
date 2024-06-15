package com.bootsnip.aichat

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bootsnip.aichat.navigation.AstraNavGraph
import com.bootsnip.aichat.ui.theme.AIChatTheme
import com.bootsnip.aichat.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.showSplash.value
            }

/*
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }


                val rotation = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.ROTATION,
                    360.0f,
                    0.0f
                )
                rotation.interpolator = OvershootInterpolator()
                rotation.duration = 500L
                rotation.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
                rotation.start()
            }
*/
        }

        setContent {
            AIChatTheme {
                AstraNavGraph()
            }
        }
    }
}