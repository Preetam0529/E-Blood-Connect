package com.php.ebloodconnect.acceptor

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.php.ebloodconnect.R

class RequestHistoryActivity : AppCompatActivity() {

    // Declare TextViews
    private lateinit var textPatientName: TextView
    private lateinit var textBloodGroup: TextView
    private lateinit var textUrgency: TextView
    private lateinit var textLocation: TextView
    private lateinit var textDate: TextView
    private lateinit var textStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_history) // Change this if your layout filename is different

        // Initialize views
        textPatientName = findViewById(R.id.textPatientName)
        textBloodGroup = findViewById(R.id.textBloodGroup)
        textUrgency = findViewById(R.id.textUrgency)
        textLocation = findViewById(R.id.textLocation)
        textDate = findViewById(R.id.textDate)
        textStatus = findViewById(R.id.textStatus)

        // Load request data
        showRequestHistory()
    }

    private fun showRequestHistory() {
        // Static values for now, can be fetched from Firebase or a local database later
        textPatientName.text = "Patient Name: Rahul Sharma"
        textBloodGroup.text = "Blood Group: B+"
        textUrgency.text = "Urgency: High"
        textLocation.text = "Hospital: AIIMS, Delhi"
        textDate.text = "Requested Date: 10-04-2025"
        textStatus.text = "Status: Fulfilled"
    }
}
