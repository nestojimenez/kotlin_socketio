package com.example.socketio.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socketio.data.models.Alarms
import com.example.socketio.data.models.Stations
import com.example.socketio.data.models.StationsWithAlarmStatus
import com.example.socketio.domain.GetAllAlarmsUseCase
import com.example.socketio.domain.GetAllStationsUseCase
import com.example.socketio.domain.GetStatusByStationUseCase
import com.example.socketio.domain.PostAlarmSupportCase
import kotlinx.coroutines.launch

class AlarmsViewModel : ViewModel() {
    val alarmsModel = MutableLiveData<List<Alarms>>()
    val isStationAlreadyAlarm = MutableLiveData<Alarms>()
    val stationsWithAlarmStatus = MutableLiveData<StationsWithAlarmStatus>()

    var getAllAlarmsCase = GetAllAlarmsUseCase()
    var getStatusByStationsCase = GetStatusByStationUseCase()


    fun onCreate(){
        viewModelScope.launch {
            Log.d("AllStations", "En rutina de onCreate")
            val result = getAllAlarmsCase()
            Log.d("AllStations", result.toString())
            alarmsModel.postValue(result)
        }
    }

    fun getStatusByStationSuspend(alStatus: Int, idStations: Int,
                                  stationsSelected: StationsWithAlarmStatus){
        Log.i("stationSelected ViewModel", stationsSelected.toString())
        viewModelScope.launch {
            val result = getStatusByStationsCase(idStations, alStatus, stationsSelected)
            Log.i("stationSelected ViewModel", stationsSelected.toString())
            if(result.isNullOrEmpty()){
                stationsWithAlarmStatus.postValue(stationsSelected)
                isStationAlreadyAlarm.postValue(Alarms(0, idStations, 0, "", ""))

            }else{
                isStationAlreadyAlarm.postValue(result[0])
            }

            Log.i("stationSelected", stationsSelected.toString())
        }
    }
}