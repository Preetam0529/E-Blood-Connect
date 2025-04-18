package com.php.ebloodconnect.acceptor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class acceptorprofileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAcceptorProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityAcceptorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Logout Button Click Listener
        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, acceptorloginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // You can also set onClickListeners for cards here if needed
        // Example:
        // binding.cardView1.setOnClickListener { /* your code */ }
    }
}