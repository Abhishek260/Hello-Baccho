package com.example.hellobaccho

import android.content.Intent
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hellobaccho.base.BaseActivity
import com.example.hellobaccho.databinding.ActivityMainBinding
import com.example.hellobaccho.ui.rickAndMortyList.RickMortyListActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor(): BaseActivity() {
    private lateinit var activityBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        setOnClick()

    }

    private fun setOnClick(){
        activityBinding.submitBtn.setOnClickListener {
            var pageNo = activityBinding.inputPageNo.text.toString()
            if (pageNo==null||pageNo==""){
                errorToast("Enter Page No")
            }
            else{
                val intent = Intent(this,RickMortyListActivity::class.java)
                intent.putExtra("pageNo", pageNo)
                startActivity(intent)
            }

        }
    }


}