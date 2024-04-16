package com.example.socketio.adapter

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socketio.data.models.Alarms
import com.example.socketio.data.network.MyApi
import com.example.socketio.R
import com.example.socketio.SocketHandler
import com.example.socketio.data.models.AlarmsProvider
import com.example.socketio.data.models.Stations
import com.example.socketio.databinding.ItemStationBinding
import com.example.socketio.data.models.StationsWithAlarmStatus
import com.example.socketio.ui.viewmodel.AlarmsViewModel
import com.google.gson.GsonBuilder
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class StationsViewHolder(
    view: View,
    private val alarmsViewModel: AlarmsViewModel,
    private val alarmsList: List<Alarms>
    /*private val lifecycle: LifecycleOwner*/
) : RecyclerView.ViewHolder(view) {

    val mSocket = SocketHandler.getSocket()

    val binding = ItemStationBinding.bind(view)

    private val BASE_URL = "http://10.105.168.231:8000"


    fun render(stationModel: Stations) {

        Log.i("AllStations", AlarmsProvider.alarms.toString())

        binding.tvIdStation.text = "Machine ID " + stationModel.id.toString()
        binding.tvNameStation.text = stationModel.st_name
        binding.tvLineStation.text = "Production Line " + stationModel.st_line
        binding.tvUnHappyOEEStation.text = "Unhappy OEE " + stationModel.st_unhappy_oee + "%"
        binding.tvHappyOEEStation.text = "Happy OEE " + stationModel.st_happy_oee + "%"
        binding.tvCreateAtStation.text = stationModel.created_at.substring(0, 10)
        binding.tvUpdatedAtStation.text = stationModel.updated_at.substring(0, 10)


       //Here we take the stations id and look into alarms to see the status of the alarm and
        //make background card color match al_status
        val red = ContextCompat.getColor(binding.ivStation.context, com.example.socketio.R.color.red)
        val currentStationAlarms =  alarmsList.find{ it.id_stations!! == stationModel.id}
        if (currentStationAlarms != null) {
            if(currentStationAlarms.al_status == 1){
                binding.card.setCardBackgroundColor(red)
            }
        }

        Glide.with(binding.ivStation.context)
            .load("http://10.105.173.111:1880/ID" + stationModel.id).into(binding.ivStation)


        itemView.setOnClickListener {

            val stationsSelected = StationsWithAlarmStatus(
                1,
                stationModel.id,
                0,
                "",
                "",
                stationModel.id,
                stationModel.st_name,
                stationModel.st_line,
                stationModel.st_unhappy_oee,
                stationModel.st_happy_oee,
                stationModel.created_at.substring(0, 10),
                stationModel.updated_at.substring(0, 10)
            )

            checkForStationsStatus(1, stationModel.id, stationsSelected)

            val alStatus = alarmsViewModel.isStationAlreadyAlarm.value
            Log.i("AlarmStatus", alStatus.toString())
            Log.i("stationSelected", stationsSelected.toString())

        }

    }


    //This function is to look if this stations is not currently alarm, if not alarm process can continue.
    private fun checkForStationsStatus(
        stationsStatus: Int,
        stationId: Int,
        stationsSelected: StationsWithAlarmStatus
    ) {
        Log.d("MyViewModel PrivateFun", "${stationsStatus} - ${stationId}")
        Log.i("stationSelected PrivateFun", stationsSelected.toString())
        alarmsViewModel.getStatusByStationSuspend(stationsStatus, stationId, stationsSelected)
        //TO DO
        //Make an observer of the isStationAlreadyAlarm to look if alarms is active or not
        /*var status = 0
        lateinit var alarma: Alarms

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            //.client(client)
            .build()
            .create(MyApi::class.java)



        api.getStatusByStation(stationId, stationsStatus)
            .enqueue(object : Callback<List<Alarms>> {
                override fun onResponse(
                    call: Call<List<Alarms>>,
                    response: Response<List<Alarms>>
                ) {
                    Log.i("getStatusByStation", response.body()!![0].al_status.toString())
                    if (response.isSuccessful) {
                        status = response.body()!![0].al_status!!

                    }
                }

                override fun onFailure(call: Call<List<Alarms>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

        return status*/
    }
}
