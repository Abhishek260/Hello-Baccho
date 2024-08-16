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
import com.example.hellobaccho.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var activityBinding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        animation()
        playSound()
    }

    private fun animation() {

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 2000
        fadeIn.fillAfter = true
        activityBinding.splashLogo.startAnimation(fadeIn)

        // Text animation (fade in with slight delay)
        val fadeInText = AlphaAnimation(0f, 1f)
        fadeInText.duration = 2000
        fadeInText.fillAfter = true
        fadeInText.startOffset = 2000

        activityBinding.splashText.visibility = android.view.View.VISIBLE
        activityBinding.splashText.startAnimation(fadeInText)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 4000)
    }

    private fun playSound() {
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