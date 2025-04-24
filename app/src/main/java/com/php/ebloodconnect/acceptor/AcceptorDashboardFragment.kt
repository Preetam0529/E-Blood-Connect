package com.php.ebloodconnect.acceptor

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

class AcceptorDashboardFragment : Fragment() {

    private lateinit var tvName: TextView
    private lateinit var tvBloodGroup: TextView
    private lateinit var tvPhone: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_acceptor_dashboard, container, false)

        // UI References
        tvName = view.findViewById(R.id.tvAcceptorName)
        tvBloodGroup = view.findViewById(R.id.tvAcceptorBloodGroup)
        tvPhone = view.findViewById(R.id.tvAcceptorPhone)

        loadAcceptorInfo()

        // Card Click Listeners
        val acceptorInfoCard: CardView = view.findViewById(R.id.acceptorInfoCard)
        val createNewRequestCard: CardView = view.findViewById(R.id.createNewRequestCard)
        val viewExistingRequestsCard: CardView = view.findViewById(R.id.viewExistingRequestsCard)
        val requestHistoryCard: CardView = view.findViewById(R.id.requestHistoryCard)
        val donorResponsesCard: CardView = view.findViewById(R.id.donorResponsesCard)

        acceptorInfoCard.setOnClickListener {
            Toast.makeText(requireContext(), "Navigating to Profile", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), AcceptorProfileActivity::class.java))
        }

        createNewRequestCard.setOnClickListener {
            Toast.makeText(requireContext(), "Creating a new request", Toast.LENGTH_SHORT).show()

            // ✅ Correct way to navigate to a fragment
            val fragment = AcceptorPostFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment) // Make sure this matches your FrameLayout ID
                .addToBackStack(null)
                .commit()
        }

        viewExistingRequestsCard.setOnClickListener {
            Toast.makeText(requireContext(), "Opening existing requests", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), viewrequestActivity::class.java))
        }

        requestHistoryCard.setOnClickListener {
            Toast.makeText(requireContext(), "Viewing request history", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), RequestHistoryActivity::class.java))
        }

        donorResponsesCard.setOnClickListener {
            Toast.makeText(requireContext(), "Checking donor responses", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), AcceptedDonorsActivity::class.java))
        }

        return view
    }

    private fun loadAcceptorInfo() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val userId = currentUser.uid

        FirebaseFirestore.getInstance()
            .collection("acceptors")
            .document(userId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    val name = doc.getString("adminName") ?: "N/A"
                    val phone = doc.getString("contact") ?: "N/A"
                    val bloodGroup = doc.getString("bloodGroup") ?: "N/A"

                    tvName.text = name
                    tvPhone.text = phone
                    tvBloodGroup.text = bloodGroup
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load acceptor info", Toast.LENGTH_SHORT).show()
            }
    }
}
