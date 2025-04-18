package com.php.ebloodconnect.donor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.php.ebloodconnect.R


class DonorFeedFragment : Fragment() {

    private lateinit var requestContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        requestContainer = view.findViewById(R.id.requestContainer)

        // Sample static data (replace with Firestore data later)
        val requests = listOf(
            DonorRequest("Ramesh Singh", "B+", 3, "+91 8765432109"),
            DonorRequest("Anita Sharma", "O-", 2, "+91 9988776655"),
            DonorRequest("Vikas Mehta", "A+", 1, "+91 9876543210")
        )

        for (request in requests) {
            val cardView = inflater.inflate(R.layout.item_donor_request_card, requestContainer, false)

            val nameText = cardView.findViewById<TextView>(R.id.textName)
            val bloodGroupText = cardView.findViewById<TextView>(R.id.textBloodGroup)
            val unitsText = cardView.findViewById<TextView>(R.id.textUnits)
            val contactText = cardView.findViewById<TextView>(R.id.textContact)
            val acceptButton = cardView.findViewById<Button>(R.id.buttonAccept)

            nameText.text = "Name: ${request.name}"
            bloodGroupText.text = "Blood Group: ${request.bloodGroup}"
            unitsText.text = "Units Needed: ${request.unitsNeeded}"
            contactText.text = "Contact: ${request.contact}"

            acceptButton.setOnClickListener {
                Toast.makeText(requireContext(), "You accepted ${request.name}'s request!", Toast.LENGTH_SHORT).show()
            }

            requestContainer.addView(cardView)
        }

        return view
    }
}
