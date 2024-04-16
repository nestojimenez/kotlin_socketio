package com.example.socketio.data.network

import com.example.socketio.data.models.Alarms
import com.example.socketio.data.models.Stations
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path


interface MyApi  {
    @GET("stations")
    fun getComments():Call<List<Stations>>

    @GET("support_alarm/{id_stations}/{al_status}")
    fun getStatusByStation(@Path("id_stations") id_stations:Int?,
                           @Path("al_status") al_status:Int?):Call<List<Alarms>>

    @Headers("Accept: application/json")
    @POST("support_alarm")
    fun createSupportAlarm(@Body alarmBody: Alarms): Call<Alarms>

    //suspend to be use with the ViewModel
    ///////////////////////////////////////
    @GET("stations")
    suspend fun getAllComments():Response<List<Stations>>

    @GET("support_alarm")
    suspend fun getAllAlarms():Response<List<Alarms>>

    @GET("support_alarm/{al_status}/{id_stations}")
    suspend fun getStatusByStationSuspend(@Path("al_status") al_status:Int?,
                           @Path("id_stations") id_stations:Int?):Response<List<Alarms>>

    @Headers("Accept: application/json")
    @POST("support_alarm")
    suspend fun createSupportAlarmSuspend(@Body alarmBody: Alarms): Response<Alarms>
}