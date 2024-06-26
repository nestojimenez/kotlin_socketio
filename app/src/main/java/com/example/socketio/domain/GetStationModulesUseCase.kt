package com.example.socketio.domain

import com.example.socketio.data.StationsRepository
import com.example.socketio.data.models.StationModules
import com.example.socketio.data.models.Users

class GetStationModulesUseCase {
    private val repository = StationsRepository()

    suspend operator fun invoke(stLine:String):List<StationModules> = repository.getStationModulesByLine(stLine)
}