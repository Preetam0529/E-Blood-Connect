package com.php.ebloodconnect.acceptor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.php.ebloodconnect.FirestoreHelper
import com.php.ebloodconnect.R

class AcceptedDonorsActivity : AppCompatActivity() {

    private lateinit var firestoreHelper: FirestoreHelper
    private lateinit var acceptedDonorContainer: LinearLayout
    private val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    private val activeCardMap = mutableMapOf<String, View>()  // Map to hold views for easy removal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accepted_donors)

        firestoreHelper = FirestoreHelper(this)
        acceptedDonorContainer = findViewById(R.id.donorCardContainer)

        loadAcceptedDonors()
    }

    private fun loadAcceptedDonors() {
        firestoreHelper.getAllRequestsForAcceptor(userId, onSuccess = { snapshot ->
            acceptedDonorContainer.removeAllViews()

            for (requestDoc in snapshot.documents) {
                val requestId = requestDoc.id
                val status = requestDoc.getString("status")

                if (status == "Accepted") {
                    firestoreHelper.getDonorConfirmationsForRequest(requestId, onSuccess = { donorSnapshot ->
                        for (donorDoc in donorSnapshot.documents) {
                            val donorId = donorDoc.getString("donorId") ?: continue
                            loadDonorDetails(donorId, requestId)
                        }
                    }, onFailure = {
                        showToast("Error fetching donor confirmations")
                    })
                }
            }
        }, onFailure = {
            showToast("Failed to load accepted requests")
        })
    }

    private fun loadDonorDetails(donorId: String, requestId: String) {
        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        db.collection("donors").document(donorId).get()
            .addOnSuccessListener { donorDoc ->
                val name = donorDoc.getString("fullName") ?: "Unknown"
                val bloodGroup = donorDoc.getString("bloodGroup") ?: "N/A"
                val contact = donorDoc.getString("contact") ?: "N/A"

                val cardView = LayoutInflater.from(this).inflate(R.layout.card_accepted_donors_item, null)
                val card = cardView.findViewById<CardView>(R.id.donorCard)
                card.findViewById<TextView>(R.id.donorName).text = "Donor: $name"
                card.findViewById<TextView>(R.id.donorBlood).text = "Blood Group: $bloodGroup"
                card.findViewById<TextView>(R.id.donorContact).text = "Contact: $contact"
                card.findViewById<TextView>(R.id.responseStatus).text = "Status: Confirmed"

                card.setOnLongClickListener {
                    showConfirmationDialog(requestId, donorId, cardView)
                    true
                }

                acceptedDonorContainer.addView(cardView)
                activeCardMap["$requestId-$donorId"] = cardView
            }
            .addOnFailureListener {
                showToast("Error loading donor details")
            }
    }

    private fun showConfirmationDialog(requestId: String, donorId: String, cardView: View) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Donation Fulfilled")
            .setMessage("Has this donation been successfully completed?")
            .setPositiveButton("Yes") { _, _ ->
                firestoreHelper.updateRequestStatus(requestId, "Fulfilled",
                    onSuccess = {
                        acceptedDonorContainer.removeView(cardView)
                        showToast("Donation marked as fulfilled.")
                    },
                    onFailure = {
                        showToast("Failed to update donation status.")
                    }
                )
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
