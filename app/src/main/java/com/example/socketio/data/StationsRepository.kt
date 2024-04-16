package com.example.socketio.data

import android.util.Log
import com.example.socketio.data.models.Alarms
import com.example.socketio.data.models.AlarmsProvider
import com.example.socketio.data.models.Stations
import com.example.socketio.data.models.StationsProvider
import com.example.socketio.data.models.StationsWithAlarmStatus
import com.example.socketio.data.models.StationsWithAlarmStatusProvider
import com.example.socketio.data.network.MyApiService

class StationsRepository {
    private val api = MyApiService()

    suspend fun getAllComments():List<Stations>{
        Log.d("Creado", "En Get all comments")
        val response:List<Stations> = api.getAllComments()
        //Log.d("Creado", "All comments getted")
        StationsProvider.stations = response
        return response
    }

    suspend fun postAlarmSupport(body: Alarms) : Alarms {
        val response: Alarms = api.postAlarmSupport(body)
        return response
    }

    suspend fun getStatusByStationSuspend(alStatus: Int, idStations: Int, stationsSelected: StationsWithAlarmStatus): List<Alarms> {
        //TODO
        Log.i("stationSelected Repository", "How many times this run")
        val response: List<Alarms> = api.getStatusByStationSuspend(alStatus, idStations, stationsSelected)
        return  response
    }

    suspend fun getAllAlarms():List<Alarms>{
        val response:List<Alarms> = api.getAllAlarms()
        AlarmsProvider.alarms = response
        return response
    }
}