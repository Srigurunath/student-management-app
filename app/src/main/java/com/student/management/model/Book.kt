package com.student.management.models

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val branch: String,
    val year: Int,
    val available: Int
)
