package com.php.ebloodconnect.acceptor

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.php.ebloodconnect.R

class acceptorloginActivity : AppCompatActivity() {

    private lateinit var hospitalNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceptorlogin)

        // Initialize views
        hospitalNameEditText = findViewById(R.id.et_hospital_admin)
        passwordEditText = findViewById(R.id.et_password)
        loginButton = findViewById(R.id.btn_login)
        registerTextView = findViewById(R.id.tv_register)

        // Login button click
        loginButton.setOnClickListener {
            val hospitalName = hospitalNameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (hospitalName.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Perform login logic (Firebase/Auth/Database)
                Toast.makeText(this, "Logged in as $hospitalName", Toast.LENGTH_SHORT).show()

                // Navigate to acceptor dashboard or home screen
                // startActivity(Intent(this, AcceptorDashboardActivity::class.java))
            }
        }

        // Navigate to register screen
        registerTextView.setOnClickListener {
            val intent = Intent(this, acceptorregisterActivity::class.java)
            startActivity(intent)
        }
    }
}
