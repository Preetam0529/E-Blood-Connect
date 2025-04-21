package com.php.ebloodconnect.acceptor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.php.ebloodconnect.FirestoreHelper
import com.php.ebloodconnect.R

class AcceptorProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestoreHelper: FirestoreHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceptor_profile)

        auth = FirebaseAuth.getInstance()
        firestoreHelper = FirestoreHelper(applicationContext)

        val logoutBtn: Button = findViewById(R.id.btn_logout)
        val deleteAccountBtn: Button = findViewById(R.id.btn_delete_account)

        logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, AcceptorLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        deleteAccountBtn.setOnClickListener {
            showDeleteAccountConfirmationDialog()
        }

        val cardAvailableUnits = findViewById<CardView>(R.id.card_available_units)
        val cardCheckIn = findViewById<CardView>(R.id.card_check_in)
        val cardPastRecords = findViewById<CardView>(R.id.card_past_records)
        val cardTodayDonation = findViewById<CardView>(R.id.card_today_donation)

        cardAvailableUnits?.setOnClickListener {
            // Navigate to available units screen
        }

        cardCheckIn?.setOnClickListener {
            // Navigate to check-in activity
        }

        cardPastRecords?.setOnClickListener {
            // Navigate to past donation records
        }

        cardTodayDonation?.setOnClickListener {
            // Navigate to today's donation summary
        }
    }

    private fun showDeleteAccountConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Account")
        builder.setMessage("Are you sure you want to delete your account? This action cannot be undone.")
        builder.setPositiveButton("Yes") { _, _ ->
            val currentUser = auth.currentUser
            if (currentUser != null) {
                deleteAccount(currentUser.uid)
            } else {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    private fun deleteAccount(userId: String) {
        firestoreHelper.deleteAcceptorAccount(userId,
            onSuccess = {
                // Delete from FirebaseAuth too
                auth.currentUser?.delete()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, AcceptorLoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to delete user auth", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onFailure = {
                Toast.makeText(this, "Failed to delete data from Firestore", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
