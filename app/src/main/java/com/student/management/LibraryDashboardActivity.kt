package com.student.management

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.management.adapters.BookAdapter
import com.student.management.models.Book
import com.student.management.network.ApiService
import com.student.management.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LibraryDashboardActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService
    private lateinit var recyclerView: RecyclerView
    private lateinit var token: String
    private var studentId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.library_dashboard)

        // Get token and student ID from Intent
        studentId = intent.getIntExtra("student_id", 0)
        token = intent.getStringExtra("token")?.let { "Bearer $it" } ?: ""

        recyclerView = findViewById(R.id.recyclerBooks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        apiService = RetrofitClient.getInstance().create(ApiService::class.java)
        loadBooks()
    }

    private fun loadBooks() {
        apiService.getBooks(token).enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                if (response.isSuccessful) {
                    val books = response.body() ?: emptyList()
                    recyclerView.adapter = BookAdapter(books)
                } else {
                    Log.e("LibraryDashboard", "Response failed: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Log.e("LibraryDashboard", "API failure: ${t.localizedMessage}")
            }
        })
    }
}
