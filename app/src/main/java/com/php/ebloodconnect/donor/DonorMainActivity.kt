package com.php.ebloodconnect.donor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.php.ebloodconnect.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class DonorMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.donorBottomNav)

        // Default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.donorFragmentContainer, DonorFeedFragment())
            .commit()

        bottomNav.setOnItemSelectedListener {
            val selectedFragment = when (it.itemId) {
                R.id.navigation_feed -> DonorFeedFragment()
                R.id.navigation_map -> DonorMapFragment()
                R.id.navigation_dashboard -> DonorDashboardFragment()
                else -> null
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.donorFragmentContainer, it)
                    .commit()
            }
            true
        }
    }
}
