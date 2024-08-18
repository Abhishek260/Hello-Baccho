package com.example.hellobaccho.base

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import android.Manifest
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity @Inject constructor(): AppCompatActivity() {
    lateinit var mContext: Context
    var progressDialog: ProgressDialog? = null
    private var prefs: SharedPreferences? = null

    fun setUpToolbar(title: String) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = title
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this@BaseActivity
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }

    open fun showProgressDialog() {
        progressDialog = ProgressDialog(mContext)
        progressDialog!!.setMessage("Loading....")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    open fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    open fun successToast(msg: String?, view: View? = null) {
        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
        playSound()
    }

    open fun errorToast(msg: String?, view: View? = null) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
        playSound()

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

