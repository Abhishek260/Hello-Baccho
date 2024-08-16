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

    }

    private fun setOnClick(){
        activityBinding.testBtn.setOnClickListener {
            val intent = Intent(this,RickMortyListActivity::class.java)
            startActivity(intent)
        }
    }


}