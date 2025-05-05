package com.student.management.ui.theme

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.student.management.R
import com.student.management.LibraryDashboardActivity
import com.student.management.ui.theme.BusStatusActivity
import com.student.management.ui.theme.PaymentHistoryActivity


class DashboardActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var studentIdText: TextView
    private lateinit var emailText: TextView
    private lateinit var logoutButton: Button
    private lateinit var libraryButton: Button
    private lateinit var stationeryButton: Button
    private lateinit var busStatusButton: Button
    private lateinit var paymentHistoryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // View bindings
        studentIdText = findViewById(R.id.studentId)
        emailText = findViewById(R.id.email)
        logoutButton = findViewById(R.id.logoutButton)
        libraryButton = findViewById(R.id.libraryButton)
        stationeryButton = findViewById(R.id.stationeryButton)
        busStatusButton = findViewById(R.id.busStatusButton)
        paymentHistoryButton = findViewById(R.id.paymentHistoryButton)

        // Data from shared prefs
        val studentId = sharedPref.getString("student_id", "-")
        val email = sharedPref.getString("email", "-")
        val token = sharedPref.getString("token", "") ?: ""

        studentIdText.text = "Student ID: $studentId"
        emailText.text = "Email: $email"

        // Button click handlers
        logoutButton.setOnClickListener {
            sharedPref.edit().clear().apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        libraryButton.setOnClickListener {
            val intent = Intent(this, LibraryDashboardActivity::class.java)
            intent.putExtra("student_id", studentId?.toIntOrNull() ?: 0)
            intent.putExtra("token", token)
            startActivity(intent)
        }

        stationeryButton.setOnClickListener {
            val intent = Intent(this, StationeryActivity::class.java)
            intent.putExtra("student_id", studentId?.toIntOrNull() ?: 0)
            intent.putExtra("token", token)
            startActivity(intent)
        }

        busStatusButton.setOnClickListener {
            val intent = Intent(this, BusStatusActivity::class.java)
            intent.putExtra("student_id", studentId?.toIntOrNull() ?: 0)
            intent.putExtra("token", token)
            startActivity(intent)
        }

        paymentHistoryButton.setOnClickListener {
            val intent = Intent(this, PaymentHistoryActivity::class.java)
            intent.putExtra("student_id", studentId?.toIntOrNull() ?: 0)
            intent.putExtra("token", token)
            startActivity(intent)
        }
    }
}
