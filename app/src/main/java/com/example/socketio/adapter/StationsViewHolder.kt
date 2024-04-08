package com.example.socketio.adapter

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socketio.data.models.Alarms
import com.example.socketio.data.network.MyApi
import com.example.socketio.R
import com.example.socketio.SocketHandler
import com.example.socketio.data.models.Stations
import com.example.socketio.databinding.ItemStationBinding
import com.example.socketio.data.models.StationsWithAlarmStatus
import com.example.socketio.ui.viewmodel.AlarmsViewModel
import com.example.socketio.ui.viewmodel.StationsViewModel
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class StationsViewHolder(view: View, private val alarmsViewModel: AlarmsViewModel) : RecyclerView.ViewHolder(view) {

    val mSocket = SocketHandler.getSocket()

    val binding = ItemStationBinding.bind(view)

    private val BASE_URL = "http://10.105.169.33:8000"



    fun render(stationModel: Stations) {

        binding.tvIdStation.text = "Machine ID " + stationModel.id.toString()
        binding.tvNameStation.text = stationModel.st_name
        binding.tvLineStation.text = "Production Line " + stationModel.st_line
        binding.tvUnHappyOEEStation.text = "Unhappy OEE " + stationModel.st_unhappy_oee + "%"
        binding.tvHappyOEEStation.text = "Happy OEE " + stationModel.st_happy_oee + "%"
        binding.tvCreateAtStation.text = stationModel.created_at.substring(0, 10)
        binding.tvUpdatedAtStation.text = stationModel.updated_at.substring(0, 10)


        Glide.with(binding.ivStation.context)
            .load("http://10.105.173.111:1880/ID" + stationModel.id).into(binding.ivStation)


        itemView.setOnClickListener {

            val alStatus = checkForStationsStatus(stationModel.id, 1)

            Toast.makeText(
                binding.ivStation.context,
                stationModel.st_name,
                Toast.LENGTH_LONG
            ).show()
            val stationsSelected = StationsWithAlarmStatus(
                1,
                stationModel.id,
                1,
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

            Log.i("AlarmStatus", alStatus.toString())

            when (alStatus) {
                0 -> showAlertWindow(stationsSelected, stationModel)
                1 -> Toast.makeText(
                    binding.ivStation.context,
                    "Stations is currently Alarmed",
                    Toast.LENGTH_LONG
                ).show()

                else -> Toast.makeText(
                    binding.ivStation.context,
                    "Option not available",
                    Toast.LENGTH_LONG
                ).show()
            }


        }

    }


    private fun showAlertWindow(
        stationsSelected: StationsWithAlarmStatus,
        stationModel: Stations
    ) {

        val dialog = Dialog(binding.ivStation.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.alert_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnContinue = dialog.findViewById<Button>(R.id.btnContinue)

        btnCancel.setOnClickListener {
            Toast.makeText(btnCancel.context, "Continue", Toast.LENGTH_LONG).show()

            dialog.dismiss()
        }

        btnContinue.setOnClickListener {

            val gsonPretty = GsonBuilder().setPrettyPrinting().create()
            mSocket.emit("station_alarm", gsonPretty.toJson(stationsSelected))

            val red = ContextCompat.getColor(binding.card.context, com.example.socketio.R.color.red)
            binding.card.setCardBackgroundColor(red)

            val newAlarmToRecord = Alarms(
                1,
                stationModel.id,
                1,
                "2024-03-18T22:34:09.000Z",
                "2024-03-18T22:34:09.000Z"
            )

            val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(client)
                .build()
                .create(MyApi::class.java)

            api.createSupportAlarm(newAlarmToRecord).enqueue(object : Callback<Alarms> {
                override fun onResponse(call: Call<Alarms>, response: Response<Alarms>) {
                    if (response.isSuccessful) {
                        Log.i("Retrofi_alarm", response.body().toString())
                    } else {
                        Log.i("Retrofi_alarmed", response.message())
                    }
                }

                override fun onFailure(call: Call<Alarms>, t: Throwable) {
                    Log.i("Retrofi_alarmx", t.message.toString())
                }
            })
            dialog.dismiss()
        }
        dialog.show()
    }

    //This function is to look if this stations is not currently alarm, if not alarm process can continue.
    private fun checkForStationsStatus(stationId: Int, stationsStatus: Int): Boolean {
        alarmsViewModel.getStatusByStationSuspend(stationId, stationsStatus)
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
