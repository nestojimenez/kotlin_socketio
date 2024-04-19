package com.example.socketio.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socketio.data.models.Alarms
import com.example.socketio.data.models.StationsWithAlarmStatus
import com.example.socketio.domain.GetAllAlarmsUseCase
import com.example.socketio.domain.GetStationWithAlarmStatusListUseCase
import com.example.socketio.domain.GetStatusByStationUseCase
import kotlinx.coroutines.launch

class AlarmsViewModel : ViewModel() {
    val alarmsModel = MutableLiveData<List<Alarms>>()
    val isStationAlreadyAlarm = MutableLiveData<Alarms>()
    val stationsSelectedWithAlarmStatus = MutableLiveData<StationsWithAlarmStatus>()
    val stationsWithAlarmStatusList = MutableLiveData<List<StationsWithAlarmStatus>>()

    var getAllAlarmsCase = GetAllAlarmsUseCase()
    var getStatusByStationsCase = GetStatusByStationUseCase()
    var getStationsWithAlarmsStatusList = GetStationWithAlarmStatusListUseCase()


    fun onCreate(){
        viewModelScope.launch {
            Log.d("AllStations", "En rutina de onCreate")
            val result = getAllAlarmsCase()
            val alarmedStations = getStationsWithAlarmsStatusList()
            Log.d("AllStations", result.toString())
            alarmsModel.postValue(result)
            stationsWithAlarmStatusList.postValue(alarmedStations)
        }
    }

    fun getStatusByStationSuspend(alStatus: Int, idStations: Int,
                                  stationsSelected: StationsWithAlarmStatus){
        Log.i("stationSelected ViewModel", stationsSelected.toString())
        viewModelScope.launch {
            val result = getStatusByStationsCase(idStations, alStatus, stationsSelected)
            Log.i("stationSelected ViewModel", stationsSelected.toString())
            if(result.isNullOrEmpty()){
                stationsSelectedWithAlarmStatus.postValue(stationsSelected)
                isStationAlreadyAlarm.postValue(Alarms(0,0, idStations, 0, "", ""))

            }else{
                stationsSelectedWithAlarmStatus.postValue(stationsSelected)
                isStationAlreadyAlarm.postValue(result[0])
            }

            Log.i("stationSelected", stationsSelected.toString())
        }
    }
}