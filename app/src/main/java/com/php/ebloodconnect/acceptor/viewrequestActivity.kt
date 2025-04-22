package com.php.ebloodconnect.acceptor

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.php.ebloodconnect.R

class viewrequestActivity : AppCompatActivity() {

    private lateinit var btnAccept: Button
    private lateinit var btnDecline: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewrequest)

        btnAccept = findViewById(R.id.btn_accept)
        btnDecline = findViewById(R.id.btn_decline)

        btnAccept.setOnClickListener {
            Toast.makeText(this, "Request Accepted ✅", Toast.LENGTH_SHORT).show()
            // Perform action like updating request status in Firestore
        }

        btnDecline.setOnClickListener {
            Toast.makeText(this, "Request Declined ❌", Toast.LENGTH_SHORT).show()
            // Perform action like deleting or marking as declined
        }
    }
}
