package com.example.hellobaccho.base

import androidx.lifecycle.MutableLiveData
import com.example.hellobaccho.api.Api
import com.example.hellobaccho.dataModels.Result
import com.example.hellobaccho.dataModels.ResultData
import org.json.JSONArray
import org.json.JSONException
import javax.inject.Inject

open class BaseRepository @Inject constructor() {
    @Inject
    lateinit var api: Api
    val SERVER_ERROR = "Something Went Wrong Please Try Again Later"
    val isError: MutableLiveData<String> = MutableLiveData()
    val viewDialogMutData: MutableLiveData<Boolean> = MutableLiveData()

    open fun getResult(response: ResultData): List<Result>? {
        return try {
            val jsonArray = response.results
            if (jsonArray.isNotEmpty()) {
                jsonArray
            } else {
                isError.postValue("No results found")
                null
            }
        } catch (e: JSONException) {
            isError.postValue("JSON parsing error")
            e.printStackTrace()
            null
        }
    }
}
