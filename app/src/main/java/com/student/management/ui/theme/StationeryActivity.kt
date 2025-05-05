package com.student.management.ui.theme

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.management.R
import com.student.management.adapter.StationeryAdapter
import com.student.management.model.StationeryItem

class StationeryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StationeryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stationery)

        recyclerView = findViewById(R.id.stationeryRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val items = listOf(
            StationeryItem("Draft Sheets", "A4, Ruled", "Add to Cart"),
            StationeryItem("Record Books", "Hardcover", "Add to Cart"),
            StationeryItem("Geometry Kit", "Pens", "Add to Cart"),
            StationeryItem("Pens", "Ballpoint", "Add to Cart"),
            StationeryItem("Pencils", "", "Buy Now"),
            StationeryItem("Buy Now", "", "Buy Now"),
            StationeryItem("Eraser", "", ""),
            StationeryItem("Sharpener", "", "")
        )

        adapter = StationeryAdapter(items) { item ->
            Toast.makeText(this, "Clicked: ${item.title}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter
    }
}
