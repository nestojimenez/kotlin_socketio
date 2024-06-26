package com.example.socketio.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socketio.data.models.Alarms
import com.example.socketio.data.models.StationModules
import com.example.socketio.domain.GetAllAlarmsUseCase
import com.example.socketio.domain.GetStationModulesUseCase
import kotlinx.coroutines.launch

class StationModulesViewModel:ViewModel() {
    val stationModel = MutableLiveData<List<StationModules>>()
    var getStationModulesUseCase = GetStationModulesUseCase()

    fun onCreate(){
        viewModelScope.launch {
            Log.d("AllStations", "En rutina de onCreate")
            val result = getStationModulesUseCase("Simplicity")
            stationModel.postValue(result)
        }
    }
}