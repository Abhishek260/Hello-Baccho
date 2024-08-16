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
import androidx.core.content.ContextCompat
import com.example.hellobaccho.dataModels.PeriodSelection
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity @Inject constructor(): AppCompatActivity() {
    lateinit var mContext: Context
    var serverErrorMsg = "Something went wrong, please try again later."
    var somethingWentWrongErrorMsg = "Something Went Wrong, Please Try Again."
    var internetError = "Internet Not Working Please Check Your Internet Connection"

    var mPeriod: MutableLiveData<PeriodSelection> = MutableLiveData()
    var timePeriod: MutableLiveData<String> = MutableLiveData()
    var imageClicked: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var storagePermission: Array<String>
    private lateinit var cameraPermission: Array<String>
    private lateinit var uri: Uri
    private lateinit var file: File
    private lateinit var camIntent: Intent
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>

    private var materialDatePicker: MaterialDatePicker<*>? = null
    private var singleDatePicker: MaterialDatePicker<*>? = null
    private var singleDatePickerWithViewType: MaterialDatePicker<*>? = null

    private var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    var progressDialog: ProgressDialog? = null
    private var prefs: SharedPreferences? = null

    private lateinit var imageLauncher: ActivityResultLauncher<Intent>
    var imageBase64List = ArrayList<String>()
    var imageBitmapList = ArrayList<Bitmap>()
    var imageUriList = ArrayList<Uri>()

    fun setUpToolbar(title: String) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = title
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this@BaseActivity
        setObservers()

        cameraPermission =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            }

        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { result ->

            }
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()

    }

    private fun setObservers() {
    }


    companion object {
        const val CAMERA_REQUEST = 100
        const val STORAGE_REQUEST = 200


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


        fun successToast(mContext: Context, msg: String?, view: View? = null) {
        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();

        }

        fun errorToast(mContext: Context, msg: String?, view: View? = null) {
            Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
        }
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



    open fun successToast(msg: String?, view: View? = null) {
        Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
        playSound()
    }

    open fun errorToast(msg: String?, view: View? = null) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
        playSound()

    }


    fun openDatePicker() {
        Log.d("BASE ACTIVITY", "PERIOD SELECTION CLICKED")
        val materialDateBuilder: MaterialDatePicker.Builder<*> =
            MaterialDatePicker.Builder.dateRangePicker()
        materialDateBuilder.setTitleText("SELECT A PERIOD")
        materialDatePicker = materialDateBuilder.build()
        materialDatePicker!!.addOnPositiveButtonClickListener { selection ->
            val viewFormat = SimpleDateFormat("dd-MM-yyyy")
            val sqlFormat = SimpleDateFormat("yyyy-MM-dd")
            val selectedDate = selection as androidx.core.util.Pair<Long?, Long?>
            if (selectedDate.first != null && selectedDate.second != null) {
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
        if (materialDatePicker != null) {
            if (materialDatePicker!!.isVisible) {
                return;
            }
            materialDatePicker!!.show(supportFragmentManager, "DATE_PICKER");
        }
//            MaterialPickerOnPositiveButtonClickListener<Pair<Long?, Long?>> { (first, second) ->
//                val viewFormat = SimpleDateFormat("MM-dd-yyyy")
//                val sqlFormat = SimpleDateFormat("yyyy-MM-dd")
//                val fromDate = Date(first)
//                val toDate = Date(second)
//                val periodSelection = PeriodSelection()
//                periodSelection.setSqlFromDate(sqlFormat.format(fromDate))
//                periodSelection.setSqlToDate(sqlFormat.format(toDate))
//                periodSelection.setViewFromDate(viewFormat.format(fromDate))
//                periodSelection.setViewToDate(viewFormat.format(toDate))
//                mPeriod.postValue(periodSelection)
//            })
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


    private fun ifPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkCameraPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            return ifPermissionGranted(Manifest.permission.READ_MEDIA_IMAGES)
                    && ifPermissionGranted(Manifest.permission.READ_MEDIA_VIDEO)
                    && ifPermissionGranted(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
                    && ifPermissionGranted(Manifest.permission.CAMERA)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ifPermissionGranted(Manifest.permission.READ_MEDIA_IMAGES)
                    && ifPermissionGranted(Manifest.permission.READ_MEDIA_VIDEO)
                    && ifPermissionGranted(Manifest.permission.CAMERA)
        } else {
            return ifPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && ifPermissionGranted(Manifest.permission.CAMERA)
        }

    }

    fun requestCameraPermission() {
        if (checkCameraPermission()) {
            return
        }
        var permissions: Array<String>? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            permissions = arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
                Manifest.permission.CAMERA
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.CAMERA
            )
        } else {
            permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        }
        if (permissions != null) {
            requestPermissions(permissions, CAMERA_REQUEST)
        } else {
            errorToast("Something went wrong. Please reinstall the app.")
        }
    }

    private fun pickFromGallery() {
        galleryLauncher.launch("image/*")
    }


    fun viewImageFullScreen() {
        startActivity(
            Intent
                (Intent.ACTION_VIEW, Uri.parse(imageBase64List.elementAt(0)))
        )
    }
}

