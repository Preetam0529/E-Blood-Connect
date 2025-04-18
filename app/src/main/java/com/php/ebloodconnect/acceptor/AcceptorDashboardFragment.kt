package com.php.ebloodconnect.acceptor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.php.ebloodconnect.R
import com.php.ebloodconnect.viewrequestactivity
import com.php.ebloodconnect.donorresponsesActivity

class AcceptorDashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_acceptor_dashboard, container, false) // Replace with your layout name if different

        // Card Click Listeners
        val createNewRequestCard: CardView = view.findViewById(R.id.createNewRequestCard)
        val viewExistingRequestsCard: CardView = view.findViewById(R.id.viewExistingRequestsCard)
        val requestHistoryCard: CardView = view.findViewById(R.id.requestHistoryCard)
        val donorResponsesCard: CardView = view.findViewById(R.id.donorResponsesCard)

        createNewRequestCard.setOnClickListener {
            Toast.makeText(requireContext(), "Creating a new request", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), AcceptorPostFragment::class.java)) // Assuming it's an Activity
        }

        viewExistingRequestsCard.setOnClickListener {
            Toast.makeText(requireContext(), "Opening existing requests", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), viewrequestactivity::class.java))
        }

        requestHistoryCard.setOnClickListener {
            Toast.makeText(requireContext(), "Viewing request history", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), RequestHistoryActivity::class.java))
        }

        donorResponsesCard.setOnClickListener {
            Toast.makeText(requireContext(), "Checking donor responses", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), donorresponsesActivity::class.java))
        }

        return view
    }
}
