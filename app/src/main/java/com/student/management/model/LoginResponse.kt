package com.student.management.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("studentId")
    val student_id: String,
    val email: String
)
