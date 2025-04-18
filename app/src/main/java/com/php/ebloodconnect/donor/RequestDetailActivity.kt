package com.php.ebloodconnect.donor

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.php.ebloodconnect.R

class RequestDetailActivity : AppCompatActivity() {
    private lateinit var requestContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_detailpagedonor)

        requestContainer = findViewById(R.id.requestContainer)

        // Sample static data
        val requests = listOf(
            DonorRequest("Ramesh Singh", "B+", 3, "+91 8765432109"),
            DonorRequest("Anita Sharma", "O-", 2, "+91 9988776655"),
            DonorRequest("Vikas Mehta", "A+", 1, "+91 9876543210")
        )

        val inflater = LayoutInflater.from(this)

        for (request in requests) {
            val cardView = inflater.inflate(R.layout.activity_request_detailpagedonor, requestContainer, false)

            val nameText = cardView.findViewById<TextView>(R.id.textName)
            val bloodGroupText = cardView.findViewById<TextView>(R.id.textBloodGroup)
            val unitsText = cardView.findViewById<TextView>(R.id.textUnits)
            val contactText = cardView.findViewById<TextView>(R.id.textContact)
            val acceptButton = cardView.findViewById<Button>(R.id.buttonAccept)

            nameText.text = "Name: ${request.name}"
            bloodGroupText.text = "Blood Group: ${request.bloodGroup}"
            unitsText.text = "Units Needed: ${request.unitsNeeded}"
            contactText.text = "Contact: ${request.contact}"

            acceptButton.setOnClickListener {
                Toast.makeText(this, "You accepted ${request.name}'s request!", Toast.LENGTH_SHORT).show()
            }

            requestContainer.addView(cardView)
        }
    }
}


