package com.php.ebloodconnect.acceptor

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.php.ebloodconnect.R
import com.php.ebloodconnect.FirestoreHelper
import java.util.*

class AcceptorRequestsFragment : Fragment() {

    private lateinit var requestContainer: LinearLayout
    private lateinit var firestoreHelper: FirestoreHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_requests, container, false)

        requestContainer = view.findViewById(R.id.requestContainer)
        firestoreHelper = FirestoreHelper(requireContext())

        fetchConfirmedDonors(inflater)

        return view
    }

    private fun fetchConfirmedDonors(inflater: LayoutInflater) {
        firestoreHelper.getAllBloodRequests(
            onSuccess = { result ->
                for (document in result) {
                    val requestId = document.id
                    val bloodGroup = document.getString("bloodGroup") ?: "N/A"
                    val contact = document.getString("contact") ?: "N/A"
                    val location = document.getString("location") ?: "N/A"

                    firestoreHelper.getConfirmedDonors(
                        requestId = requestId,
                        onSuccess = { donorResults ->
                            for (donorDocument in donorResults) {
                                val donorId = donorDocument.getString("donorId") ?: "Unknown"

                                val cardView = inflater.inflate(R.layout.fragment_feed, null)
                                    .findViewById<CardView>(R.id.item_donor_request_card)

                                val nameText = cardView.findViewById<TextView>(R.id.textName)
                                val bloodGroupText = cardView.findViewById<TextView>(R.id.textBloodGroup)
                                val contactText = cardView.findViewById<TextView>(R.id.textContact)
                                val locationText = cardView.findViewById<TextView>(R.id.textUnits)
                                val acceptButton = cardView.findViewById<Button>(R.id.buttonAccept)
                                val declineButton = cardView.findViewById<Button>(R.id.buttonDecline)

                                nameText.text = "Donor: $donorId"
                                bloodGroupText.text = "Blood Group: $bloodGroup"
                                contactText.text = "Contact: $contact"
                                locationText.text = "Location: $location"

                                acceptButton.setOnClickListener {
                                    showScheduleDialog { scheduledDateTime ->
                                        firestoreHelper.acceptDonationRequest(
                                            requestId,
                                            donorId,
                                            scheduledDateTime,
                                            onSuccess = {
                                                Toast.makeText(requireContext(), "Donation accepted & donor scheduled", Toast.LENGTH_SHORT).show()
                                            },
                                            onFailure = {
                                                Toast.makeText(requireContext(), "Failed to accept donation", Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                }

                                declineButton.setOnClickListener {
                                    firestoreHelper.declineDonationRequest(
                                        requestId,
                                        donorId,
                                        onSuccess = {
                                            Toast.makeText(requireContext(), "Donation declined", Toast.LENGTH_SHORT).show()
                                        },
                                        onFailure = {
                                            Toast.makeText(requireContext(), "Failed to decline donation", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }

                                requestContainer.addView(cardView)
                            }
                        },
                        onFailure = {
                            Toast.makeText(requireContext(), "Failed to load donor confirmations", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            },
            onFailure = {
                Toast.makeText(requireContext(), "Failed to load requests", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun showScheduleDialog(onScheduleSet: (String) -> Unit) {
        val calendar = Calendar.getInstance()

        DatePickerDialog(requireContext(), { _, year, month, day ->
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val dateTime = String.format("%04d-%02d-%02d %02d:%02d", year, month + 1, day, hour, minute)
                onScheduleSet(dateTime)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
}
