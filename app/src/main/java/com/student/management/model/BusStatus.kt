package com.student.management.models

data class BusStatus(
    val busNumber: String,
    val distance: String,
    val delay: String,
    val locationUrl: String // optional for map preview if needed
)
