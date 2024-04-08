package com.example.socketio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socketio.R
import com.example.socketio.data.models.Stations
import com.example.socketio.ui.viewmodel.AlarmsViewModel


class StationsAdapter(private val stationsList:List<Stations>, private val alarmsViewModel:AlarmsViewModel) : RecyclerView.Adapter<StationsViewHolder>(){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return StationsViewHolder(layoutInflater.inflate(R.layout.item_station, parent, false), alarmsViewModel)
    }

    override fun getItemCount(): Int {
        return stationsList.size
    }

    override fun onBindViewHolder(holder: StationsViewHolder, position: Int) {
        val item = stationsList[position]
        holder.render(item)

    }

}