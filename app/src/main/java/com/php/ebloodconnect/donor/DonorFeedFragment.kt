package com.php.ebloodconnect.donor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.php.ebloodconnect.R
import com.php.ebloodconnect.FirestoreHelper

class DonorFeedFragment : Fragment() {

    private lateinit var requestContainer: LinearLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var firestoreHelper: FirestoreHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)

        requestContainer = view.findViewById(R.id.requestContainer)
        auth = FirebaseAuth.getInstance()
        firestoreHelper = FirestoreHelper(requireContext())

        fetchRequests(inflater)

        return view
    }

    private fun fetchRequests(inflater: LayoutInflater) {
        firestoreHelper.getAllBloodRequests(
            onSuccess = { result ->
                requestContainer.removeAllViews()
                for (document in result) {
                    val requestId = document.id
                    val name = document.getString("name") ?: "N/A"
                    val bloodGroup = document.getString("bloodGroup") ?: "N/A"
                    val contact = document.getString("contact") ?: "N/A"
                    val requiredDate = document.getString("requiredDate") ?: "N/A"
                    val location = document.getString("location") ?: "N/A"
                    val urgency = document.getString("urgency") ?: "N/A"

                    val cardView = inflater.inflate(R.layout.item_blood_request, requestContainer, false) as CardView

                    val nameText = cardView.findViewById<TextView>(R.id.textName)
                    val bloodGroupText = cardView.findViewById<TextView>(R.id.textBloodGroup)
                    val unitsText = cardView.findViewById<TextView>(R.id.textUnits)
                    val contactText = cardView.findViewById<TextView>(R.id.textContact)
                    val acceptButton = cardView.findViewById<Button>(R.id.buttonAccept)

                    nameText.text = "Name: $name"
                    bloodGroupText.text = "Blood Group: $bloodGroup"
                    unitsText.text = "Urgency: $urgency\nDate: $requiredDate"
                    contactText.text = "Contact: $contact\nLocation: $location"

                    acceptButton.setOnClickListener {
                        val donorId = auth.currentUser?.uid ?: return@setOnClickListener
                        firestoreHelper.confirmDonation(
                            requestId,
                            donorId,
                            onSuccess = {
                                Toast.makeText(
                                    requireContext(),
                                    "You have accepted to donate to $name",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onFailure = {
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to confirm donation",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }

                    requestContainer.addView(cardView)
                }
            },
            onFailure = {
                Toast.makeText(requireContext(), "Failed to load requests", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
