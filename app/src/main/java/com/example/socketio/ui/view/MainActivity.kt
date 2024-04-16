package com.example.socketio.ui.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socketio.data.models.Alarms
import com.example.socketio.R
import com.example.socketio.SocketHandler
import com.example.socketio.adapter.StationsAdapter
import com.example.socketio.data.models.AlarmsProvider
import com.example.socketio.databinding.ActivityMainBinding
import com.example.socketio.data.models.Stations
import com.example.socketio.data.models.StationsProvider
import com.example.socketio.data.models.StationsWithAlarmStatus
import com.example.socketio.data.network.MyApi
import com.example.socketio.ui.viewmodel.AlarmsViewModel
import com.example.socketio.ui.viewmodel.StationsViewModel
import com.google.gson.GsonBuilder
import io.socket.client.Socket
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    var counter: Int = 0
    var stationAlarmed = ""

    var stationsList = listOf<Stations>(Stations(1, "Prueba", "Cequr", "40%", "70%", "", ""))

    private val BASE_URL = "http://10.105.168.231:8000"//"https://jsonplaceholder.typicode.com"
    private val TAG: String = "CHECK_RESPONSE"
    private val stationsViewModel: StationsViewModel by viewModels()
    private val alarmsViewModel: AlarmsViewModel by viewModels()

    lateinit var mSocket: Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setContentView(R.layout.activity_main)

        stationsViewModel.onCreate()
        alarmsViewModel.onCreate()

        stationsViewModel.stationModel.observe(this, { currentStations ->
            binding.tvTitle.text = currentStations[0].st_name
            getAllComments(currentStations)
        })

        alarmsViewModel.alarmsModel.observe(this, { alarms ->
            Log.i("AlarmsUpdated After Acknowledge", alarms.toString())
            Log.i("AlarmsUpdated After Acknowledge", StationsProvider.stations.toString())
            getAllComments(StationsProvider.stations)
        })

        SocketHandler.setSocket()

        mSocket = SocketHandler.getSocket()

        mSocket.connect()


        mSocket.on("station_alarm_for_user") { args ->
            Log.i("Args", args[0].toString())
            if (args[0] != null) {
                //counter = args[0] as Int
                stationAlarmed = args[0] as String
                runOnUiThread {

                }
            }
        }

        //getAllComments()
        alarmsViewModel.isStationAlreadyAlarm.observe(this, { alarmStatus ->
            Log.i("MyViewModel Adapter", alarmStatus.toString())
            val stationsSelected = alarmsViewModel.stationsWithAlarmStatus.value
            if (alarmStatus != null) {
                if (alarmStatus.al_status == 0) {
                    Log.i("stationSelected ViewHolder", stationsSelected.toString())
                    showAlertWindow(stationsSelected!!, alarmStatus.id_stations!!)
                }
                //TODO by now only add else option, but as we progress more alstatus options may be included
                else {
                    Toast.makeText(this, "Station Already Alarmed!!!", Toast.LENGTH_LONG).show()
                }

            }
            //getAllComments()

        })
    }

    private fun initStationRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_stations)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StationsAdapter(stationsList, alarmsViewModel, AlarmsProvider.alarms)
    }

    private fun getAllComments(currentStations: List<Stations>) {
        val manager = LinearLayoutManager(this@MainActivity)
        binding.rvStations.layoutManager = manager
        binding.rvStations.adapter =
            StationsAdapter(currentStations, alarmsViewModel, AlarmsProvider.alarms)
    }


    private fun showAlertWindow(
        stationsSelected: StationsWithAlarmStatus,
        idStations: Int
    ) {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.alert_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tvStation = dialog.findViewById<TextView>(R.id.tvStation)
        tvStation.text = stationsSelected.st_name
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnContinue = dialog.findViewById<Button>(R.id.btnContinue)

        btnCancel.setOnClickListener {
            Toast.makeText(btnCancel.context, "Continue", Toast.LENGTH_LONG).show()

            dialog.dismiss()
        }

        btnContinue.setOnClickListener {

            val gsonPretty = GsonBuilder().setPrettyPrinting().create()
            //Here stations alarmed is send to socketio, revisar

            //val mSocket = SocketHandler.getSocket()
            //mSocket.connect()

            //update al_status with next status to be share on socketio
            Log.i("stationSelected ViewHolder", stationsSelected.toString())
            val stationSelectedNextStatus = StationsWithAlarmStatus(stationsSelected.AlarmId,
                stationsSelected.id_stations, 1, stationsSelected.createdAt,
                stationsSelected.updatedAt, stationsSelected.id, stationsSelected.st_name, stationsSelected.st_line,
                stationsSelected.st_unhappy_oee, stationsSelected.st_happy_oee, stationsSelected.created_at, stationsSelected.updated_at)

            mSocket.emit("station_alarm", gsonPretty.toJson(stationSelectedNextStatus))

            //val red = ContextCompat.getColor(this, com.example.socketio.R.color.red)
            //binding.card.setCardBackgroundColor(red)

            val newAlarmToRecord = Alarms(
                1,
                stationsSelected.id_stations,
                1,
                "2024-03-18T22:34:09.000Z",
                "2024-03-18T22:34:09.000Z"
            )
            //Modify al_status on alarmsViewModel.alarms to one as is been activated
            //Add new alarms stations to alarmsViewmodel.alarms

            Log.i("alarmsModel", alarmsViewModel.alarmsModel.value.toString())
            val alarmsUpdated = (alarmsViewModel.alarmsModel.value)?.toMutableList()

            if (alarmsUpdated != null) {
                alarmsUpdated.add(newAlarmToRecord)
                Log.i("alarmsModel", alarmsUpdated.toString())
                alarmsViewModel.alarmsModel.postValue(alarmsUpdated)
                AlarmsProvider.alarms = alarmsUpdated
            }


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
}
