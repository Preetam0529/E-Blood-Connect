package com.php.ebloodconnect.donor

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.php.ebloodconnect.R

class DonorDashboardFragment : Fragment() {

    private lateinit var tvDonorName: TextView
    private lateinit var tvBloodGroup: TextView
    private lateinit var tvPhone: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donor_dashboard, container, false)

        // Reference to Donor Info TextViews
        tvDonorName = view.findViewById(R.id.tvDonorName)
        tvBloodGroup = view.findViewById(R.id.tvBloodGroup)
        tvPhone = view.findViewById(R.id.tvPhone)

        loadDonorInfo()

        // Reference to cards
        val donorinfoCard: CardView = view.findViewById(R.id.donorInfoCard)
        val availableRequestsCard: CardView = view.findViewById(R.id.availableRequestsCard)
        val upcomingAppointmentsCard: CardView = view.findViewById(R.id.upcomingAppointmentsCard)
        val pastDonationsCard: CardView = view.findViewById(R.id.pastDonationsCard)

        donorinfoCard.setOnClickListener {
            Toast.makeText(requireContext(), "Navigating to Profile", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), DonorProfile::class.java))
        }

        availableRequestsCard.setOnClickListener {
            Toast.makeText(requireContext(), "Navigating to Requests", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), DonorFeedFragment::class.java))
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

    private fun loadDonorInfo() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val userId = currentUser.uid

        FirebaseFirestore.getInstance()
            .collection("donors")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("name") ?: "N/A"
                    val bloodGroup = document.getString("bloodGroup") ?: "N/A"
                    val phone = document.getString("phone") ?: "N/A"

                    tvDonorName.text = name
                    tvBloodGroup.text = bloodGroup
                    tvPhone.text = phone
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load donor info", Toast.LENGTH_SHORT).show()
            }
    }
}
