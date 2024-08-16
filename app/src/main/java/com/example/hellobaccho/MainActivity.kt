package com.example.hellobaccho

import android.content.Intent
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hellobaccho.databinding.ActivityMainBinding
import com.example.hellobaccho.ui.rickAndMortyList.RickMortyListActivity

class MainActivity : AppCompatActivity() {
    private lateinit var activityBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setOnClick()
        playSound()
    }

    private fun setOnClick(){
        activityBinding.testBtn.setOnClickListener {
            val intent = Intent(this,RickMortyListActivity::class.java)
            startActivity(intent)
        }
    }

    open fun playSound() {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}