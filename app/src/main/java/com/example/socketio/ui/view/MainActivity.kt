package com.example.socketio.ui.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socketio.data.models.Alarms
import com.example.socketio.R
import com.example.socketio.SocketHandler
import com.example.socketio.adapter.StationsAdapter
import com.example.socketio.databinding.ActivityMainBinding
import com.example.socketio.data.models.Stations
import com.example.socketio.data.network.MyApi
import com.example.socketio.ui.viewmodel.AlarmsViewModel
import com.example.socketio.ui.viewmodel.StationsViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private lateinit var binding:ActivityMainBinding

    var counter: Int = 0
    var stationAlarmed = ""

    var stationsList = listOf<Stations>(Stations(1, "Prueba", "Cequr", "40%", "70%", "", ""))

    private val BASE_URL = "http://10.105.169.33:8000"//"https://jsonplaceholder.typicode.com"
    private val TAG :String = "CHECK_RESPONSE"
    private val stationsViewModel : StationsViewModel by viewModels()
    private val alarmsViewModel: AlarmsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setContentView(R.layout.activity_main)

        stationsViewModel.onCreate()

        stationsViewModel.stationModel.observe(this, { currentStations ->
            binding.tvTitle.text = currentStations[0].st_name
            getAllComments(currentStations)
        })

        SocketHandler.setSocket()

        val mSocket = SocketHandler.getSocket()

        mSocket.connect()


        mSocket.on("station_alarm_for_user") {args ->
            Log.i("Args", args[0].toString())
            if(args[0]!= null){
                //counter = args[0] as Int
                stationAlarmed = args[0] as String
                runOnUiThread {

                }


            }
        }

        //getAllComments()
    }

    private fun initStationRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.rv_stations)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StationsAdapter(stationsList, alarmsViewModel)
    }

    private fun getAllComments(currentStations: List<Stations>){
        val manager = LinearLayoutManager(this@MainActivity)
        binding.rvStations.layoutManager = manager
        binding.rvStations.adapter = StationsAdapter(currentStations, alarmsViewModel)
    }

    /*private fun getAllComments(){

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)

        api.getComments().enqueue(object : Callback<List<Stations>> {
            override fun onResponse(
                call: Call<List<Stations>>,
                response: Response<List<Stations>>
            ) {
                if(response.isSuccessful){


                    val manager = LinearLayoutManager(this@MainActivity)

                    binding.rvStations.layoutManager = manager
                    binding.rvStations.adapter = StationsAdapter(response.body()!!)
                    //binding.rvStations.addItemDecoration(decoration)

                    response.body()?.let{
                        for(comment in it){
                            Log.i(TAG, "onResponse: ${comment.st_line}")

                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<Stations>>, t: Throwable) {
                Log.i(TAG, "onFailure: ${t.message}")
            }

        })
    }*/

    fun testPost() {
        val newAlarm = Alarms(1, 4, 1, "2023-03-18T22:34:09.000Z", "2023-03-18T22:34:09.000Z")
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)

        api.createSupportAlarm(newAlarm).enqueue(object: Callback<Alarms>{
            override fun onResponse(call: Call<Alarms>, response: Response<Alarms>) {
                if(response.isSuccessful){
                    Log.i("Retrofi_alarm", response.body().toString())
                }else{
                    Log.i("Retrofi_alarmed", response.toString())
                }
            }

            override fun onFailure(call: Call<Alarms>, t: Throwable) {
                Log.i("Retrofi_alarm", t.message.toString())
            }

        })
    }
}
