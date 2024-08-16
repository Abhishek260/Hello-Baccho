package com.example.hellobaccho.ui.splashScreen

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.os.Handler
import android.os.Looper
import com.example.hellobaccho.MainActivity
import com.example.hellobaccho.R
import com.example.hellobaccho.base.BaseActivity
import com.example.hellobaccho.databinding.ActivitySplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreen @Inject constructor(): BaseActivity() {
    private lateinit var activityBinding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        animation()
        playMusic()
    }

    private fun animation() {

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 3000
        fadeIn.fillAfter = true
        activityBinding.splashLogo.startAnimation(fadeIn)

        val fadeInText = AlphaAnimation(0f, 1f)
        fadeInText.duration = 3000
        fadeInText.fillAfter = true
        fadeInText.startOffset = 3000

        activityBinding.splashText.visibility = android.view.View.VISIBLE
        activityBinding.splashText.startAnimation(fadeInText)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 6000)
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

}