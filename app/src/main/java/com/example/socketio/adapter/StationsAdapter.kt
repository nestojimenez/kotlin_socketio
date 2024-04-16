package com.example.socketio.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.socketio.R
import com.example.socketio.data.models.Alarms
import com.example.socketio.data.models.Stations
import com.example.socketio.ui.viewmodel.AlarmsViewModel


class StationsAdapter(private val stationsList:List<Stations>, private val alarmsViewModel:AlarmsViewModel, private val alarmsList:List<Alarms>) : RecyclerView.Adapter<StationsViewHolder>(){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        //TODO move oberserver to mainactivity
        /*alarmsViewModel.isStationAlreadyAlarm.observe(parent.findViewTreeLifecycleOwner()!!, { alarmStatus ->
            Log.i("MyViewModel Adapter", alarmStatus.toString())
            val stationsSelected = alarmsViewModel.stationsWithAlarmStatus.value
            if(alarmStatus != null){
                if(alarmStatus.al_status == 0){
                    Log.i("stationSelected ViewHolder", stationsSelected.toString())
                    //showAlertWindow(stationsSelected!!, alarmStatus.id_stations!!)
                }

            }

            //showAlertWindow(stationsSelected!!, alarmStatus[0].id_stations!!)
        })*/

        return StationsViewHolder(layoutInflater.inflate(R.layout.item_station, parent, false)
            , alarmsViewModel, alarmsList/*, parent.findViewTreeLifecycleOwner()!!*/)
    }

    override fun getItemCount(): Int {
        return stationsList.size
    }

    override fun onBindViewHolder(holder: StationsViewHolder, position: Int) {

        val item = stationsList[position]
        holder.render(item)

    }

}