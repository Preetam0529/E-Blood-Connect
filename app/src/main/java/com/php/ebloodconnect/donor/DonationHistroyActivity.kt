package com.php.ebloodconnect.donor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.php.ebloodconnect.R

class DonationHistroyActivity : AppCompatActivity() {

    // In a real scenario, data would be fetched from Firestore or an API
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_histroy)

        // TODO: Add Firebase Firestore data fetching & populate cards dynamically if needed
    }
}
