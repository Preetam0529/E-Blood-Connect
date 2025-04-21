package com.php.ebloodconnect.acceptor

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.php.ebloodconnect.FirestoreHelper
import com.php.ebloodconnect.R

class AcceptorLoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var hospitalNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceptorlogin)

        auth = FirebaseAuth.getInstance()

        hospitalNameEditText = findViewById(R.id.et_hospital_admin)
        passwordEditText = findViewById(R.id.et_password)
        loginButton = findViewById(R.id.btn_login)
        registerTextView = findViewById(R.id.tv_register)

        loginButton.setOnClickListener {
            loginUser()
        }

        registerTextView.setOnClickListener {
            startActivity(Intent(this, AcceptorRegisterActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, AcceptorMainActivity::class.java))
            finish()
        }
    }

    private fun loginUser() {
        val email = hospitalNameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userId = auth.currentUser?.uid ?: return@addOnSuccessListener
                FirestoreHelper(this).saveUserRole(
                    userId,
                    "acceptor",
                    onSuccess = {
                        startActivity(Intent(this, AcceptorMainActivity::class.java))
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
