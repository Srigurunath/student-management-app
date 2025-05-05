package com.student.management.ui.theme

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.student.management.R
import com.student.management.network.ApiService
import com.student.management.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var etStudentId: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etStudentId = findViewById(R.id.etStudentId)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        loginButton = findViewById(R.id.btnLogin)

        apiService = RetrofitClient.getInstance().create(ApiService::class.java)

        loginButton.setOnClickListener {
            val studentIdInput = etStudentId.text.toString().trim()
            val emailInput = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if ((studentIdInput.isEmpty() && emailInput.isEmpty()) || password.isEmpty()) {
                Toast.makeText(this, "Enter Student ID or Email + Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = mutableMapOf<String, String>()
            if (studentIdInput.isNotEmpty()) request["studentId"] = studentIdInput  // ✅ Fixed key
            if (emailInput.isNotEmpty()) request["email"] = emailInput
            request["password"] = password

            loginStudent(request)
        }
    }

    private fun loginStudent(request: Map<String, String>) {
        apiService.loginStudent(request).enqueue(object : Callback<Map<String, String>> {
            override fun onResponse(
                call: Call<Map<String, String>>,
                response: Response<Map<String, String>>
            ) {
                if (response.isSuccessful) {
                    val token = response.body()?.get("access_token") ?: ""
                    val studentId = response.body()?.get("studentId") ?: ""   // ✅ Match backend response key
                    val email = response.body()?.get("email") ?: ""

                    val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    intent.putExtra("token", token)
                    intent.putExtra("student_id", studentId)
                    intent.putExtra("email", email)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Login failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
