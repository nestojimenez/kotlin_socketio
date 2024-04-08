package com.example.socketio.data.network

import android.util.Log
import com.example.socketio.core.RetrofitHelper
import com.example.socketio.data.models.Alarms
import com.example.socketio.data.models.Stations
import com.example.socketio.data.models.StationsWithAlarmStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class MyApiService {

    private val retrofit = RetrofitHelper.getRetrofit()
    suspend fun getAllComments():List<Stations>{
        return withContext(Dispatchers.IO){
            val response: Response<List<Stations>> = retrofit.create(MyApi::class.java).getAllComments()
            response.body() ?: emptyList()
        }

    }

    suspend fun postAlarmSupport(body : Alarms):Alarms{
        return withContext(Dispatchers.IO){
            val response: Response<Alarms> = retrofit.create(MyApi::class.java).createSupportAlarmSuspend(body)
            response.body() ?: Alarms(0, 0, 0, "", "")
        }
    }

    suspend fun getStatusByStationSuspend(idStations :Int, alStatus: Int):List<Alarms>{
        return withContext(Dispatchers.IO){
            val response: Response<List<Alarms>> = retrofit.create(MyApi::class.java).getStatusByStationSuspend(idStations, alStatus)
            response.body() ?: emptyList()
        }
    }
}