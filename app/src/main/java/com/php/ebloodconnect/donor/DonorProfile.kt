package com.php.ebloodconnect.donor

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.php.ebloodconnect.FirestoreHelper
import com.php.ebloodconnect.MainActivity
import com.php.ebloodconnect.R
import com.php.ebloodconnect.UserAuthentication

class DonorProfile : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var logoutButton: Button
    private lateinit var firestoreHelper: FirestoreHelper
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donorprofile)

        profileImage = findViewById(R.id.imageProfile)
        editButton = findViewById(R.id.buttonEditDetails)
        deleteButton = findViewById(R.id.buttonDeleteAccount)
        logoutButton = findViewById(R.id.buttonLogout)

        auth = FirebaseAuth.getInstance()
        firestoreHelper = FirestoreHelper(this)

        profileImage.setOnClickListener {
            Toast.makeText(this, "Profile image clicked!", Toast.LENGTH_SHORT).show()
        }

        editButton.setOnClickListener {
            Toast.makeText(this, "Edit Details Clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to edit screen
        }

        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, UserAuthentication::class.java)
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
                deleteAccount()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteAccount() {
        val user = auth.currentUser
        val userId = user?.uid ?: return

        firestoreHelper.deleteDonorData(userId,
            onSuccess = {
                user.delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, DonorLoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to delete user from Auth", Toast.LENGTH_SHORT).show()
                    }
            },
            onFailure = {
                Toast.makeText(this, "Failed to delete donor data: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
