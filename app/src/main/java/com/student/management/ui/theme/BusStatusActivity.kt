package com.student.management.ui.theme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.management.R
import com.student.management.adapters.BusStatusAdapter
import com.student.management.models.BusStatus

class BusStatusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_status)

        // RecyclerView reference
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerBusStatus)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Dummy bus data (static for now, can be fetched from backend later)
        val dummyBusList = listOf(
            BusStatus("Bus 2105", "2.5 mi away", "Delayed 5 min", "https://example.com/map1.png"),
            BusStatus("Bus 1732", "1.1 mi away", "On Time", "https://example.com/map2.png"),
            BusStatus("Bus 3042", "3.8 mi away", "Arriving Soon", "https://example.com/map3.png")
        )
        // Set adapter
        recyclerView.adapter = BusStatusAdapter(dummyBusList)
    }
}
