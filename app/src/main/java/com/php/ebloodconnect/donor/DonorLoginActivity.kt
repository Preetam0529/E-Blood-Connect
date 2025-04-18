package com.php.ebloodconnect.donor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.php.ebloodconnect.R


class DonorLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donorlogin)

        // Initialize Views
        val usernameEditText = findViewById<EditText>(R.id.Username)
        val passwordEditText = findViewById<EditText>(R.id.pass)
        val loginButton = findViewById<Button>(R.id.btn_submit)
        val registerTextView = findViewById<TextView>(R.id.registerHere)

        // Login Button Click
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            } else {
                // Perform authentication logic here (Add Firebase or SQLite authentication later)
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                // Navigate to next screen (e.g., DonorDashboardActivity)
            }
        }

        // Register Here Click
        registerTextView.setOnClickListener {
            val intent = Intent(this, DonorRegisterActivity::class.java) // Replace with your Register Activity
            startActivity(intent)
        }
    }
}
