package com.php.ebloodconnect.acceptor

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.php.ebloodconnect.R
import java.text.SimpleDateFormat
import java.util.*

class AcceptorPostFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var bloodGroupEditText: EditText
    private lateinit var dateTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var contactEditText: EditText
    private lateinit var urgencySpinner: Spinner
    private lateinit var submitButton: Button
    private lateinit var progressBar: ProgressBar

    private val calendar = Calendar.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private var acceptorLocation: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post, container, false)

        nameEditText = view.findViewById(R.id.editTextName)
        bloodGroupEditText = view.findViewById(R.id.editTextBloodGroup)
        dateTextView = view.findViewById(R.id.textViewDate)
        locationTextView = view.findViewById(R.id.textViewLocation)
        contactEditText = view.findViewById(R.id.editTextContact)
        urgencySpinner = view.findViewById(R.id.spinnerUrgency)
        submitButton = view.findViewById(R.id.buttonSubmit)
        progressBar = view.findViewById(R.id.progressBar)

        setupDatePicker()
        setupUrgencySpinner()
        fetchAcceptorLocation()
        submitButton.setOnClickListener { submitRequest() }

        return view
    }

    private fun setupDatePicker() {
        dateTextView.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                dateTextView.text = format.format(calendar.time)
            }, year, month, day).show()
        }
    }

    private fun setupUrgencySpinner() {
        val urgencyLevels = arrayOf("Low", "Medium", "High", "Critical")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, urgencyLevels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        urgencySpinner.adapter = adapter
    }

    private fun fetchAcceptorLocation() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("acceptors").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    acceptorLocation = document.getString("location") ?: "Not Available"
                    locationTextView.text = "Location: $acceptorLocation"
                } else {
                    Toast.makeText(requireContext(), "Acceptor location not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error fetching location", Toast.LENGTH_SHORT).show()
            }
    }

    private fun submitRequest() {
        val name = nameEditText.text.toString().trim()
        val bloodGroup = bloodGroupEditText.text.toString().trim()
        val requiredDate = dateTextView.text.toString().trim()
        val contact = contactEditText.text.toString().trim()
        val urgency = urgencySpinner.selectedItem.toString()
        val userId = auth.currentUser?.uid ?: return

        if (name.isEmpty() || bloodGroup.isEmpty() || requiredDate.isEmpty() || contact.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        val request = hashMapOf(
            "name" to name,
            "bloodGroup" to bloodGroup,
            "requiredDate" to requiredDate,
            "location" to acceptorLocation,
            "contact" to contact,
            "urgency" to urgency,
            "acceptorId" to userId,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("bloodRequests")
            .add(request)
            .addOnSuccessListener {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Request posted successfully", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Failed to post request", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        nameEditText.text.clear()
        bloodGroupEditText.text.clear()
        dateTextView.text = "Select Required Date"
        contactEditText.text.clear()
        urgencySpinner.setSelection(0)
    }
}
