package com.php.ebloodconnect.acceptor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.php.ebloodconnect.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AcceptorMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceptor_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.acceptorBottomNav)

        // Default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.acceptorFragmentContainer, AcceptorRequestsFragment())
            .commit()

        bottomNav.setOnItemSelectedListener {
            val selectedFragment = when (it.itemId) {
                R.id.navigation_requests -> AcceptorRequestsFragment()
                R.id.navigation_post -> AcceptorPostFragment()
                R.id.navigation_acceptor_map -> AcceptorMapFragment()
                R.id.navigation_acceptor_dashboard -> AcceptorDashboardFragment()
                else -> null
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.acceptorFragmentContainer, it as Fragment)
                    .commit()
            }
            true
        }
    }
}
