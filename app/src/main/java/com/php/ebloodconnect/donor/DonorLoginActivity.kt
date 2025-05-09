package com.php.ebloodconnect.donor

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.php.ebloodconnect.FirestoreHelper
import com.php.ebloodconnect.R

class DonorLoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donorlogin)

        auth = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.Username)
        passwordEditText = findViewById(R.id.pass)
        loginButton = findViewById(R.id.btn_submit)
        registerTextView = findViewById(R.id.registerHere)

        loginButton.setOnClickListener {
            loginUser()
        }

        registerTextView.setOnClickListener {
            startActivity(Intent(this, DonorRegisterActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User already logged in, go to DonorMainActivity directly
            startActivity(Intent(this, DonorMainActivity::class.java))
            finish()
        }
    }

    private fun loginUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userId = auth.currentUser?.uid ?: return@addOnSuccessListener
                FirestoreHelper(this).saveUserRole(
                    userId,
                    "donor",
                    onSuccess = {
                        startActivity(Intent(this, DonorMainActivity::class.java))
                        finish()
                    },
                    onFailure = {
                        Toast.makeText(this, "Failed to save role", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            .addOnFailureListener {
                Toast.makeText(this, "Login failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
