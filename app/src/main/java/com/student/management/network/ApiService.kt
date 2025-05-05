package com.student.management.network

import com.student.management.model.BorrowBookRequest
import com.student.management.models.Book
import com.student.management.models.PaymentRecord
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("student_login")
    fun loginStudent(
        @Body loginData: Map<String, String>
    ): Call<Map<String, String>>

    @POST("borrow_book")
    fun borrowBook(
        @Header("Authorization") token: String,
        @Body request: BorrowBookRequest
    ): Call<Map<String, String>>

    @POST("return_book")
    fun returnBook(
        @Header("Authorization") token: String,
        @Body request: Map<String, Int>
    ): Call<Map<String, String>>

    @GET("bus_status")
    fun getBusStatus(
        @Header("Authorization") token: String,
        @Query("student_id") studentId: Int
    ): Call<Map<String, String>>

    @GET("payment_history")
    fun getPaymentHistory(
        @Header("Authorization") token: String,
        @Query("student_id") studentId: Int
    ): Call<List<PaymentRecord>>


    @GET("get_books")
    fun getBooks(
        @Header("Authorization") token: String
    ): Call<List<Book>>
}
