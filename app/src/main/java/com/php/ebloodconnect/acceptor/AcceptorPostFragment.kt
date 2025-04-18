package com.php.ebloodconnect.acceptor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.php.ebloodconnect.R

class AcceptorPostFragment : Fragment() {
    private lateinit var nameEditText: EditText
    private lateinit var bloodGroupEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var contactEditText: EditText
    private lateinit var urgencySpinner: Spinner
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        // Initialize views
        nameEditText = view.findViewById(R.id.editTextName)
        bloodGroupEditText = view.findViewById(R.id.editTextBloodGroup)
        dateEditText = view.findViewById(R.id.editTextDate)
        locationEditText = view.findViewById(R.id.editTextLocation)
        contactEditText = view.findViewById(R.id.editTextContact)
        urgencySpinner = view.findViewById(R.id.spinnerUrgency)
        submitButton = view.findViewById(R.id.buttonSubmit)

        // Set up urgency spinner
        val urgencyOptions = resources.getStringArray(R.array.urgency_levels)
        val urgencyAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            urgencyOptions
        )
        urgencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        urgencySpinner.adapter = urgencyAdapter

        // Handle button click
        submitButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val bloodGroup = bloodGroupEditText.text.toString()
            val date = dateEditText.text.toString()
            val location = locationEditText.text.toString()
            val contact = contactEditText.text.toString()
            val urgency = urgencySpinner.selectedItem.toString()

            if (name.isBlank() || bloodGroup.isBlank() || date.isBlank() ||
                location.isBlank() || contact.isBlank() || urgency == "Select Urgency"
            ) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // TODO: Submit the request to database / server
                Toast.makeText(requireContext(), "Request Submitted Successfully", Toast.LENGTH_LONG).show()
                clearForm()
            }
        }

        return view
    }

    private fun clearForm() {
        nameEditText.text.clear()
        bloodGroupEditText.text.clear()
        dateEditText.text.clear()
        locationEditText.text.clear()
        contactEditText.text.clear()
        urgencySpinner.setSelection(0)
    }
}
