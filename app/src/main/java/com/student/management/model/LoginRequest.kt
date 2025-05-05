package com.student.management.model

data class LoginRequest(
    val studentId: String?,
    val email: String?,
    val password: String
)
