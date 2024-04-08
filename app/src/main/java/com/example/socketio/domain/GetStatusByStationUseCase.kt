package com.example.socketio.domain

import com.example.socketio.data.StationsRepository
import com.example.socketio.data.models.Alarms
import com.example.socketio.data.models.Stations

class GetStatusByStationUseCase {
    private val repository = StationsRepository()

    suspend operator fun invoke(idStation: Int, alStatus: Int):List<Alarms>? = repository.getStatusByStationSuspend(idStation, alStatus)
}