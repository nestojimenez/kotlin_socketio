package com.example.socketio.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socketio.data.models.Alarms
import com.example.socketio.data.models.Stations
import com.example.socketio.domain.GetAllStationsUseCase
import com.example.socketio.domain.GetStatusByStationUseCase
import com.example.socketio.domain.PostAlarmSupportCase
import kotlinx.coroutines.launch

class AlarmsViewModel : ViewModel() {
    val alarmsModel = MutableLiveData<Alarms>()
    val isStationAlreadyAlarm = MutableLiveData<Boolean>()

    var postAlarmSupportCase = PostAlarmSupportCase()
    var getStatusByStationsCase = GetStatusByStationUseCase()

    fun onCreate(body: Alarms){
        viewModelScope.launch {
            Log.d("Creado", "En rutina de onCreate")
            val result = postAlarmSupportCase(body)
            Log.d("Creado", "Get all stations correcto")
            if(result!!.al_status!=0){
                alarmsModel.postValue(result)
                Log.d("Creado", "En rutina de postvalue")
            }
        }
    }

    fun getStatusByStationSuspend(idStations: Int, alStatus: Int){
        viewModelScope.launch {
            val result = getStatusByStationsCase(idStations, alStatus)

            if(!result.isNullOrEmpty()){
                isStationAlreadyAlarm.postValue(false)
            }else{
                isStationAlreadyAlarm.postValue(true)
            }
        }
    }
}