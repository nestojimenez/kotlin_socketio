package com.example.socketio.data.network

import android.util.Log
import com.example.socketio.core.RetrofitHelper
import com.example.socketio.data.models.Alarms
import com.example.socketio.data.models.StationModules
import com.example.socketio.data.models.Stations
import com.example.socketio.data.models.StationsWithAlarmStatus
import com.example.socketio.data.models.Users
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
            response.body() ?: Alarms(0,1, 0, 0, "", "", "")
        }
    }

    suspend fun getStatusByStationSuspend( idStations :Int, alStatus: Int, stationsSelected: StationsWithAlarmStatus):List<Alarms>{
        Log.i("ApiService", "${alStatus} - ${idStations}")
        return withContext(Dispatchers.IO){
            val response: Response<List<Alarms>> = retrofit.create(MyApi::class.java).getStatusByStationSuspend(alStatus,idStations)
            response.body() ?: emptyList()
        }
    }

    suspend fun getAllAlarms():List<Alarms>{
        return withContext(Dispatchers.IO){
            val response: Response<List<Alarms>> = retrofit.create(MyApi::class.java).getAllAlarms()
            Log.i("ApiService", response.body().toString())
            response.body() ?: emptyList()
        }

    }

    suspend fun getAllUsers():List<Users>{
        return withContext(Dispatchers.IO){
            val response: Response<List<Users>> = retrofit.create(MyApi::class.java).getAllUsers()
            response.body() ?: emptyList()
        }

    }

    suspend fun getAllStationsWithAlarmsStatus():List<StationsWithAlarmStatus>{
        return withContext(Dispatchers.IO){
            val response: Response<List<StationsWithAlarmStatus>> = retrofit.create(MyApi::class.java).getAllStationsWithAlarmsStatus()
            response.body() ?: emptyList()
        }
    }

    suspend fun getStationModulesByLine(stLine:String):List<StationModules>{
        return withContext(Dispatchers.IO){
            val response: Response<List<StationModules>> = retrofit.create(MyApi::class.java).getStationModulesByLine(stLine)
            response.body() ?: emptyList()
        }
    }
}