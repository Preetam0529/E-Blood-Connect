package com.php.ebloodconnect.donor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.php.ebloodconnect.R

// import com.example.ebloodconnect.DonationHistoryActivity // Uncomment if this exists

class DonorDashboardFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donor_dashboard, container, false)

        // Find views
        val availableRequestsCard: CardView = view.findViewById(R.id.availableRequestsCard)
        val upcomingAppointmentsCard: CardView = view.findViewById(R.id.upcomingAppointmentsCard)
        val pastDonationsCard: CardView = view.findViewById(R.id.pastDonationsCard)

        // Set click listeners
        availableRequestsCard.setOnClickListener {
            Toast.makeText(requireContext(), "Navigating to Requests", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), RequestDetailActivity::class.java))
        }

        upcomingAppointmentsCard.setOnClickListener {
            Toast.makeText(requireContext(), "Checking Appointments", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), AppointmentsActivity::class.java))
        }

        pastDonationsCard.setOnClickListener {
            Toast.makeText(requireContext(), "Showing Donation History", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), DonationHistoryActivity::class.java))
        }

        return view
    }
}
