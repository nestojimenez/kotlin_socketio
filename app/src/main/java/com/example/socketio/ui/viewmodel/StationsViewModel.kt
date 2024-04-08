package com.example.socketio.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socketio.data.models.Stations
import com.example.socketio.domain.GetAllStationsUseCase
import kotlinx.coroutines.launch

class StationsViewModel : ViewModel() {

    val stationModel = MutableLiveData<List<Stations>>()

    var getAllStationsUseCase = GetAllStationsUseCase()

    fun onCreate(){
        viewModelScope.launch {
            Log.d("Creado", "En rutina de onCreate")
            val result = getAllStationsUseCase()
            Log.d("Creado", "Get all stations correcto")
            if(!result.isNullOrEmpty()){
                stationModel.postValue(result)
                Log.d("Creado", "En rutina de postvalue")
            }
        }
    }
}