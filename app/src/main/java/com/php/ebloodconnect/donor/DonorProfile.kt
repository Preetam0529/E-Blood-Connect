package com.php.ebloodconnect.donor

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.php.ebloodconnect.MainActivity
import com.php.ebloodconnect.R

class DonorProfile : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donorprofile)

        profileImage = findViewById(R.id.imageProfile)
        editButton = findViewById(R.id.buttonEditDetails)
        deleteButton = findViewById(R.id.buttonDeleteAccount)
        logoutButton = findViewById(R.id.buttonLogout)

        // Profile Image Click (optional action)
        profileImage.setOnClickListener {
            Toast.makeText(this, "Profile image clicked!", Toast.LENGTH_SHORT).show()
        }

        // Edit Button
        editButton.setOnClickListener {
            Toast.makeText(this, "Edit Details Clicked", Toast.LENGTH_SHORT).show()
            // Navigate to edit screen if implemented
        }

        // Delete Button with confirmation
        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        // Logout Button
        logoutButton.setOnClickListener {
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            // Perform actual logout and redirect to login screen
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to permanently delete your account?")
            .setPositiveButton("Yes") { _, _ ->
                Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show()
                // Add delete logic
            }
            .setNegativeButton("No", null)
            .show()
    }
}
