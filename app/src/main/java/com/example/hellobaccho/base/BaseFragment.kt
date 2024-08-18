package com.example.hellobaccho.base

import android.R
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.hellobaccho.dataModels.PeriodSelection
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment @Inject constructor(): BottomSheetDialogFragment() {
    lateinit var mContext: Context
    private var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    var progressDialog: ProgressDialog? = null
    private var prefs: SharedPreferences? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext=this@BaseFragment.requireContext()

    }





    open fun showProgressDialog() {
        progressDialog = ProgressDialog(mContext)
        progressDialog!!.setMessage("Loading....")
        progressDialog!!.setCancelable(false)
        //    progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog!!.show()
    }

    open fun hideProgressDialog() {
        progressDialog?.dismiss()
    }


    open fun successToast(msg: String?) {
        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();

    }

    open fun errorToast(msg: String?) {
        playSound()
                Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
    }



    open fun playSound() {
        try {
            // Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.error_sound);
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(mContext, notification)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}