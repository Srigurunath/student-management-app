package com.student.management.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.student.management.R
import com.student.management.model.StationeryItem

class StationeryAdapter(
    private val items: List<StationeryItem>,
    private val onClick: (StationeryItem) -> Unit
) : RecyclerView.Adapter<StationeryAdapter.StationeryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationeryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stationery, parent, false)
        return StationeryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StationeryViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class StationeryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.itemTitle)
        private val descText: TextView = itemView.findViewById(R.id.itemDescription)
        private val actionButton: Button = itemView.findViewById(R.id.actionButton)

        fun bind(item: StationeryItem) {
            titleText.text = item.title
            descText.text = item.description
            actionButton.text = item.buttonText

            actionButton.setOnClickListener {
                onClick(item)
            }
        }
    }
}
