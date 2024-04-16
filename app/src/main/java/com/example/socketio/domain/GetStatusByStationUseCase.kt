package com.example.socketio.domain

import com.example.socketio.data.StationsRepository
import com.example.socketio.data.models.Alarms
import com.example.socketio.data.models.Stations
import com.example.socketio.data.models.StationsWithAlarmStatus

class GetStatusByStationUseCase {
    private val repository = StationsRepository()

    suspend operator fun invoke(alStatus: Int,
                                idStation: Int,
                                stationsSelected: StationsWithAlarmStatus):
            List<Alarms>? = repository.getStatusByStationSuspend(alStatus, idStation, stationsSelected)
}