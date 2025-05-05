package com.student.management.ui.theme

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.management.PaymentHistoryAdapter
import com.student.management.R
import com.student.management.models.PaymentRecord
import com.student.management.network.ApiService
import com.student.management.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentHistoryActivity : AppCompatActivity() {

    private lateinit var recyclerPayment: RecyclerView
    private lateinit var apiService: ApiService
    private var studentId: Int = 0
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_history)

        // Get passed data
        studentId = intent.getIntExtra("student_id", 0)
        token = intent.getStringExtra("token") ?: ""

        // Setup RecyclerView
        recyclerPayment = findViewById(R.id.recyclerPayment)
        recyclerPayment.layoutManager = LinearLayoutManager(this)

        // API setup
        apiService = RetrofitClient.getInstance().create(ApiService::class.java)
        fetchPaymentHistory()
    }

    private fun fetchPaymentHistory() {
        apiService.getPaymentHistory("Bearer $token", studentId)
            .enqueue(object : Callback<List<PaymentRecord>> {
                override fun onResponse(
                    call: Call<List<PaymentRecord>>,
                    response: Response<List<PaymentRecord>>
                ) {
                    if (response.isSuccessful) {
                        val payments = response.body() ?: emptyList()
                        recyclerPayment.adapter = PaymentHistoryAdapter(payments)
                    } else {
                        Log.e("PaymentHistory", "Failed: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<PaymentRecord>>, t: Throwable) {
                    Log.e("PaymentHistory", "Error: ${t.localizedMessage}")
                }
            })
    }
}
