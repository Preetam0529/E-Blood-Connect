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
import com.google.firebase.auth.FirebaseAuth
import com.php.ebloodconnect.FirestoreHelper
import com.php.ebloodconnect.R
import java.util.*

class AcceptorRequestsFragment : Fragment() {

    private lateinit var requestContainer: LinearLayout
    private lateinit var firestoreHelper: FirestoreHelper
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_requests, container, false)

        requestContainer = view.findViewById(R.id.requestContainer)
        firestoreHelper = FirestoreHelper(requireContext())
        auth = FirebaseAuth.getInstance()

        val currentUserId = auth.currentUser?.uid
        if (currentUserId != null) {
            loadRequests(currentUserId, inflater)
        }

        return view
    }

    private fun loadRequests(userId: String, inflater: LayoutInflater) {
        firestoreHelper.getAllRequestsForAcceptor(
            userId,
            onSuccess = { requestsSnapshot ->
                requestContainer.removeAllViews()
                for (requestDoc in requestsSnapshot) {
                    val requestId = requestDoc.id
                    val bloodGroup = requestDoc.getString("bloodGroup") ?: "N/A"
                    val contact = requestDoc.getString("contact") ?: "N/A"
                    val location = requestDoc.getString("locationName") ?: "N/A"

                    firestoreHelper.getDonorConfirmationsForRequest(
                        requestId,
                        onSuccess = { confirmationsSnapshot ->
                            for (confirmationDoc in confirmationsSnapshot) {
                                val donorId = confirmationDoc.getString("donorId") ?: continue
                                val status = confirmationDoc.getString("status") ?: "pending"

                                firestoreHelper.getDonorDetails(
                                    donorId,
                                    onSuccess = { donorData ->
                                        val cardView = inflater.inflate(R.layout.item_donor_request_card, requestContainer, false) as CardView

                                        val nameText = cardView.findViewById<TextView>(R.id.textName)
                                        val bloodGroupText = cardView.findViewById<TextView>(R.id.textBloodGroup)
                                        val contactText = cardView.findViewById<TextView>(R.id.textContact)
                                        val locationText = cardView.findViewById<TextView>(R.id.textUnits)
                                        val acceptButton = cardView.findViewById<Button>(R.id.buttonAccept)
                                        val declineButton = cardView.findViewById<Button>(R.id.buttonDecline)

                                        val name = donorData?.get("fullName") ?: "Donor"
                                        val donorBloodGroup = donorData?.get("bloodGroup") ?: bloodGroup
                                        val donorContact = donorData?.get("contact") ?: contact

                                        nameText.text = "Donor: $name"
                                        bloodGroupText.text = "Blood Group: $donorBloodGroup"
                                        contactText.text = "Contact: $donorContact"
                                        locationText.text = "Location: $location"

                                        when (status) {
                                            "Accepted" -> {
                                                acceptButton.visibility = View.GONE
                                                declineButton.visibility = View.GONE
                                                Toast.makeText(requireContext(), "$name already accepted", Toast.LENGTH_SHORT).show()
                                            }
                                            "Declined" -> {
                                                acceptButton.visibility = View.GONE
                                                declineButton.visibility = View.GONE
                                                cardView.alpha = 0.5f
                                            }
                                            else -> {
                                                acceptButton.setOnClickListener {
                                                    showScheduleDialog { scheduledDateTime ->
                                                        firestoreHelper.acceptDonationRequest(
                                                            requestId,
                                                            donorId,
                                                            scheduledDateTime,
                                                            onSuccess = {
                                                                firestoreHelper.updateDonorConfirmationStatus(
                                                                    requestId,
                                                                    donorId,
                                                                    "Accepted",
                                                                    onSuccess = {
                                                                        Toast.makeText(requireContext(), "Donation accepted & scheduled", Toast.LENGTH_SHORT).show()
                                                                    },
                                                                    onFailure = {
                                                                        Toast.makeText(requireContext(), "Failed to update confirmation", Toast.LENGTH_SHORT).show()
                                                                    }
                                                                )
                                                            },
                                                            onFailure = {
                                                                Toast.makeText(requireContext(), "Failed to accept", Toast.LENGTH_SHORT).show()
                                                            }
                                                        )
                                                    }
                                                }

                                                declineButton.setOnClickListener {
                                                    firestoreHelper.declineDonationRequest(
                                                        requestId,
                                                        donorId,
                                                        onSuccess = {
                                                            firestoreHelper.updateDonorConfirmationStatus(
                                                                requestId,
                                                                donorId,
                                                                "Declined",
                                                                onSuccess = {
                                                                    Toast.makeText(requireContext(), "Donation declined", Toast.LENGTH_SHORT).show()
                                                                    requestContainer.removeView(cardView)
                                                                },
                                                                onFailure = {
                                                                    Toast.makeText(requireContext(), "Failed to update status", Toast.LENGTH_SHORT).show()
                                                                }
                                                            )
                                                        },
                                                        onFailure = {
                                                            Toast.makeText(requireContext(), "Failed to decline", Toast.LENGTH_SHORT).show()
                                                        }
                                                    )
                                                }
                                            }
                                        }

                                        requestContainer.addView(cardView)
                                    },
                                    onFailure = {
                                        Toast.makeText(requireContext(), "Failed to load donor details", Toast.LENGTH_SHORT).show()
                                    }
                                )
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
                val scheduledTime = String.format("%04d-%02d-%02d %02d:%02d", year, month + 1, day, hour, minute)
                onScheduleSet(scheduledTime)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
}
