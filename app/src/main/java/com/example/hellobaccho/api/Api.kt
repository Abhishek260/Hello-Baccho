package com.example.hellobaccho.api
import com.example.hellobaccho.dataModels.ResultData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("character")
    fun rickAndMortyList(
        @Query("page") pageNo: String?,
    ): Call<ResultData>

    @GET("WMS/GetJeenaDashBoard")
    fun getUserMenu(
        @Query("prmconnstring") companyId: String,
        @Query("prmusercode") userCode:String?,
        @Query("prmbranchcode") branchCode:String?,
        @Query("prmfromdt") fromDt: String?,
        @Query("prmtodt") toDt: String?,
        @Query("prmappversion") appVersion: String?,
    ): Call<ResultData>


}