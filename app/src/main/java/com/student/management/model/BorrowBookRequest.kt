package com.student.management.model

data class BorrowBookRequest(
    val studentId: String,
    val bookId: String
)
