package com.example.hellobaccho.ui.splashScreen

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hellobaccho.ui.introPage.MainActivity
import com.example.hellobaccho.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity @Inject constructor() : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreenContent()
        }
        playMusic()
    }

    private fun playMusic() {
        try {
            val soundUri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.music)
            val mediaPlayer = MediaPlayer.create(applicationContext, soundUri)
            mediaPlayer?.start()
            mediaPlayer?.setOnCompletionListener {
                it.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Composable
    fun SplashScreenContent() {
        var logoAlpha by remember { mutableStateOf(0f) }
        var textAlpha by remember { mutableStateOf(0f) }

        // Animation for the logo
        LaunchedEffect(Unit) {
            logoAlpha = 1f
            delay(3000)
            textAlpha = 1f
            delay(3000)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            }, 3000)
        }

        // Animated values
        val animatedLogoAlpha = animateFloatAsState(targetValue = logoAlpha, label = "")
        val animatedTextAlpha = animateFloatAsState(targetValue = textAlpha, label = "")

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().background(Color.White)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.splash),
                    contentDescription = stringResource(id = R.string.app_name),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .alpha(animatedLogoAlpha.value)
                        .size(350.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.hello_baccho),
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive,
                    color = Color.Black,
                    modifier = Modifier.alpha(animatedTextAlpha.value)
                )
            }
        }
    }
}
