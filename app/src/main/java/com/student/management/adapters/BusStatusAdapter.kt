package com.student.management.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.management.R
import com.student.management.models.BusStatus

class BusStatusAdapter(private val statusList: List<BusStatus>) :
    RecyclerView.Adapter<BusStatusAdapter.BusStatusViewHolder>() {

    class BusStatusViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val busNumber: TextView = view.findViewById(R.id.busNumber)
        val distance: TextView = view.findViewById(R.id.distance)
        val delay: TextView = view.findViewById(R.id.delay)
        val mapView: ImageView = view.findViewById(R.id.mapView)  // FIXED ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStatusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bus_status_card, parent, false)
        return BusStatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: BusStatusViewHolder, position: Int) {
        val item = statusList[position]
        holder.busNumber.text = item.busNumber
        holder.distance.text = item.distance
        holder.delay.text = item.delay

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(item.locationUrl)
            .placeholder(R.drawable.static_map_bus)
            .into(holder.mapView)
    }

    override fun getItemCount(): Int = statusList.size
}
