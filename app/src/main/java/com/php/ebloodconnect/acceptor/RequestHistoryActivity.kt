package com.php.ebloodconnect.acceptor

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.php.ebloodconnect.FirestoreHelper
import com.php.ebloodconnect.R

class RequestHistoryActivity : AppCompatActivity() {

    private lateinit var requestContainer: LinearLayout
    private val auth = FirebaseAuth.getInstance()
    private val firestoreHelper = FirestoreHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_history)

        requestContainer = findViewById(R.id.requestContainer)
        val currentUserId = auth.currentUser?.uid

        if (currentUserId != null) {
            loadHistory(currentUserId)
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadHistory(userId: String) {
        firestoreHelper.getAllRequestsForAcceptor(
            userId,
            onSuccess = onSuccess@{ documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "No history found.", Toast.LENGTH_SHORT).show()
                    return@onSuccess
                }

                val inflater = LayoutInflater.from(this)

                for (doc in documents) {
                    val card = inflater.inflate(R.layout.card_request_history_item, requestContainer, false) as CardView

                    val name = doc.getString("name") ?: "N/A"
                    val bloodGroup = doc.getString("bloodGroup") ?: "N/A"
                    val urgency = doc.getString("urgency") ?: "N/A"
                    val location = doc.getString("location") ?: "N/A"
                    val date = doc.getString("date") ?: "N/A"
                    val status = doc.getString("status") ?: "Pending"

                    card.findViewById<TextView>(R.id.textPatientName).text = "Patient Name: $name"
                    card.findViewById<TextView>(R.id.textBloodGroup).text = "Blood Group: $bloodGroup"
                    card.findViewById<TextView>(R.id.textUrgency).text = "Urgency: $urgency"
                    card.findViewById<TextView>(R.id.textLocation).text = "Hospital: $location"
                    card.findViewById<TextView>(R.id.textDate).text = "Requested Date: $date"
                    card.findViewById<TextView>(R.id.textStatus).text = "Status: $status"

                    val color = when (status.lowercase()) {
                        "accepted" -> R.color.light_green
                        "fulfilled" -> R.color.light_blue
                        else -> R.color.light_yellow
                    }
                    card.setCardBackgroundColor(ContextCompat.getColor(this, color))

                    requestContainer.addView(card)
                }
            },
            onFailure = {
                Toast.makeText(this, "Failed to fetch history.", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
