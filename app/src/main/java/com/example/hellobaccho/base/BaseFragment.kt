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
    var serverErrorMsg = "Something Went Wrong Please Try Again Later"
    var internetError = "Internet Not Working Please Check Your Internet Connection"

    var mPeriod: MutableLiveData<PeriodSelection> = MutableLiveData()
    var timePeriod: MutableLiveData<String> = MutableLiveData()



    private var materialDatePicker: MaterialDatePicker<*>? = null
    private var singleDatePicker: MaterialDatePicker<*>? = null
    //    var timePicker: TimePicker? = null
    private var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    var progressDialog: ProgressDialog? = null
    private var prefs: SharedPreferences? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext=this@BaseFragment.requireContext()

        setObservers()
    }


    private fun setObservers() {

//        capturedImage.observe(this) { imageUri ->
//            successToast("BASE_ACTIVITY ${imageUri.path}")
//        }
    }



    companion object {
        //        cameraSetup
        const val CAMERA_REQUEST = 100
        const val STORAGE_REQUEST = 200

        var capturedImage: MutableLiveData<Uri> = MutableLiveData()

        open fun getSqlCurrentDate(): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date = Date()
            return formatter.format(date)
        }

        open fun getViewCurrentDate(): String {
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            val date = Date()
            return formatter.format(date)
        }

        open fun getSqlCurrentTime(): String {
            val formatter = SimpleDateFormat("HH:mm")
            val date = Date()
            return formatter.format(date)
        }
    }


//    open fun openCommonBottomSheet(mContext: Context, title: String, bottomSheetClick: BottomSheetClick<Any>, commonList: ArrayList<CommonBottomSheetModel<Any>>, withAdapter: Boolean = false, index: Int = -1) {
////        showProgressDialog()
//        val bottomSheetDialog = CommonBottomSheet.newInstance(mContext, title, bottomSheetClick, commonList, withAdapter, index)
////        hideProgressDialog()
//        bottomSheetDialog.show(parentFragmentManager, CommonBottomSheet.TAG)
//    }


    private fun getSharedPref(): SharedPreferences {
        if(this.prefs != null) {
            return prefs!!
        }
//        prefs = getPreferences(MODE_PRIVATE)
//        prefs = getSharedPreferences("KGS_PRINTING_APP", MODE_PRIVATE)
//        prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        prefs = activity?.getSharedPreferences(activity?.packageName, Context.MODE_PRIVATE);
        return prefs!!
    }

    fun saveStorageBoolean(tag: String, data: Boolean) {
        prefs = getSharedPref()
        val prefEditor = prefs?.edit()
        prefEditor?.putBoolean(tag, data)
        prefEditor?.apply()
    }
    fun getStorageBoolean(tag: String): Boolean {
        prefs = getSharedPref()
        return prefs?.getBoolean(tag, false)!!
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


    fun saveStorageString(tag: String, data: String) {
        prefs = getSharedPref()
        val prefEditor = prefs?.edit()
        prefEditor?.putString(tag, data)
        prefEditor?.apply()
    }
    fun getStorageString(tag: String): String {
        prefs = getSharedPref()
        return prefs?.getString(tag, "")!!
    }

    fun clearStorage() {
        prefs = getSharedPref()
        val prefEditor = prefs?.edit()
        prefEditor?.clear()
        prefEditor?.apply()
    }



    open fun successToast(msg: String?) {
        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();

    }

    open fun errorToast(msg: String?) {
        playSound()
                Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
    }


    fun openDatePicker() {
        Log.d("BASE ACTIVITY", "PERIOD SELECTION CLICKED")
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.dateRangePicker()
        materialDateBuilder.setTitleText("SELECT A PERIOD")
        materialDatePicker = materialDateBuilder.build()
        materialDatePicker!!.addOnPositiveButtonClickListener { selection ->
            val viewFormat = SimpleDateFormat("MM-dd-yyyy")
            val sqlFormat = SimpleDateFormat("yyyy-MM-dd")
            val selectedDate = selection as androidx.core.util.Pair<Long?, Long?>
            if(selectedDate.first != null && selectedDate.second != null) {
                val fromDate = Date(selectedDate.first!!)
                val toDate = Date(selectedDate.second!!)
                val periodSelection = PeriodSelection()
                periodSelection.sqlFromDate = sqlFormat.format(fromDate)
                periodSelection.sqlToDate = sqlFormat.format(toDate)
                periodSelection.viewFromDate = viewFormat.format(fromDate)
                periodSelection.viewToDate = viewFormat.format(toDate)
                mPeriod.postValue(periodSelection)
            }
        }
        if(materialDatePicker != null) {
            if (materialDatePicker!!.isVisible) {
                return;
            }
            materialDatePicker!!.show(parentFragmentManager, "DATE_PICKER");
        }

    }

    open fun openSingleDatePicker() {
        Log.d("BASE ACTIVITY", "SINGLE PERIOD SELECTION")
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText("SELECT A DATE")
        singleDatePicker = materialDateBuilder.build()
        singleDatePicker!!.addOnPositiveButtonClickListener{ selection: Any ->
            val viewFormat = SimpleDateFormat("MM-dd-yyyy")
            val sqlFormat = SimpleDateFormat("yyyy-MM-dd")
            val selectedDate = selection
            val singleDate = Date(selectedDate as Long)
            val periodSelection = PeriodSelection()
            periodSelection.sqlsingleDate = sqlFormat.format(singleDate)
            periodSelection.viewsingleDate = viewFormat.format(singleDate)
            mPeriod.postValue(periodSelection)
        }
        if(singleDatePicker != null) {
            if (singleDatePicker!!.isVisible) {
                return;
            }
            singleDatePicker!!.show(parentFragmentManager, "DATE_PICKER");
        }
    }



    open fun getSqlDate(): String? {
        dateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        return dateFormat.format(Date())
    }


    open fun getViewDate(): String? {
        dateFormat = SimpleDateFormat("dd-MM-yyyy")
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        return dateFormat.format(Date())
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