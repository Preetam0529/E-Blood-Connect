package com.php.ebloodconnect

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.php.ebloodconnect.donor.DonorMainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Delay for 2 seconds before starting the MainActivity
        Handler().postDelayed({
            // Start MainActivity
            val intent = Intent(this, UserAuthentication::class.java)
            startActivity(intent)
            finish() // Finish the SplashActivity so the user can't go back to it
        }, 2000) // 2000 milliseconds = 2 seconds
    }
}