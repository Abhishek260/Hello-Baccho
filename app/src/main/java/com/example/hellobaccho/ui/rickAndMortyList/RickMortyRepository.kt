package com.example.hellobaccho.ui.rickAndMortyList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hellobaccho.base.BaseRepository
import com.example.hellobaccho.dataModels.Result
import com.example.hellobaccho.dataModels.ResultData
import com.example.hellobaccho.ui.rickAndMortyList.models.RickMortyListModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RickMortyRepository @Inject constructor() : BaseRepository() {

    private val rickMortyListMuteLiveData = MutableLiveData<ArrayList<Result>>()

    val viewDialogLiveData: LiveData<Boolean>
        get() = viewDialogMutData

    val rickMortyLiveData: LiveData<ArrayList<Result>>
        get() = rickMortyListMuteLiveData

    fun getRickMortyList(pageNo: String?) {
        viewDialogMutData.postValue(true)
        api.rickAndMortyList(pageNo).enqueue(object : Callback<ResultData> {
            override fun onResponse(call: Call<ResultData?>, response: Response<ResultData>) {
                viewDialogMutData.postValue(false)
                if (response.body() != null) {
                    val result = response.body()!!
                    val jsonArray = getResult(result)
                    rickMortyListMuteLiveData.postValue(jsonArray as ArrayList<Result>)

                } else {
                    isError.postValue(SERVER_ERROR)
                }
            }

            override fun onFailure(call: Call<ResultData?>, t: Throwable) {
                viewDialogMutData.postValue(false)
                isError.postValue(t.message)
            }
        })
    }
}
