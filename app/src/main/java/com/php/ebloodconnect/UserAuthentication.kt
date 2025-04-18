package com.php.ebloodconnect

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.php.ebloodconnect.acceptor.acceptorloginActivity
import com.php.ebloodconnect.donor.DonorLoginActivity

class UserAuthentication : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userauthentication)

        val donorButton: ImageButton = findViewById(R.id.DonorButton)
        val acceptorButton: ImageButton = findViewById(R.id.AcceptorButton)

        donorButton.setOnClickListener {
            // Navigate to DonorLoginActivity
            val intent = Intent(this, DonorLoginActivity::class.java)
            startActivity(intent)
        }

        acceptorButton.setOnClickListener {
            // Navigate to AcceptorRegistrationActivity (You need to create this class)
             val intent = Intent(this, acceptorloginActivity::class.java)
             startActivity(intent)
        }
    }
}
