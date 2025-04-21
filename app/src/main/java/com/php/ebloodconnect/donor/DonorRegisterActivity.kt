package com.php.ebloodconnect.donor

import com.php.ebloodconnect.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.php.ebloodconnect.FirestoreHelper
import java.util.*

class DonorRegisterActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var spinnerGender: Spinner
    private lateinit var etDOB: EditText
    private lateinit var etEmail: EditText
    private lateinit var spinnerBloodGroup: Spinner
    private lateinit var spinnerLocation: Spinner
    private lateinit var btnSubmit: Button

    private lateinit var firestoreHelper: FirestoreHelper
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donorregister)

        // Initialize Firebase and Firestore helper
        auth = FirebaseAuth.getInstance()
        firestoreHelper = FirestoreHelper(this)

        // Binding views
        etFullName = findViewById(R.id.etFullName)
        spinnerGender = findViewById(R.id.spinnerGender)
        etDOB = findViewById(R.id.etDOB)
        etEmail = findViewById(R.id.etEmail)
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup)
        spinnerLocation = findViewById(R.id.spinnerLocation)
        btnSubmit = findViewById(R.id.btn_submit2)

        // Gender dropdown
        val genderOptions = listOf("Select Gender", "Male", "Female", "Other")
        spinnerGender.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Blood group dropdown
        val bloodGroups = listOf("Select Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        spinnerBloodGroup.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bloodGroups).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Location dropdown
        val locations = listOf("Select City", "Mumbai", "Delhi", "Bangalore", "Hyderabad", "Ahmedabad", "Chennai", "Kolkata", "Pune", "Jaipur", "Lucknow")
        spinnerLocation.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, locations).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Date Picker for DOB
        etDOB.setOnClickListener {
            showDatePickerDialog { date -> etDOB.setText(date) }
        }

        // Submit button
        btnSubmit.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val gender = spinnerGender.selectedItem.toString()
            val dob = etDOB.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val bloodGroup = spinnerBloodGroup.selectedItem.toString()
            val location = spinnerLocation.selectedItem.toString()

            if (fullName.isBlank() || gender == "Select Gender" || dob.isBlank() ||
                email.isBlank() || bloodGroup == "Select Blood Group" || location == "Select City"
            ) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = auth.currentUser?.uid
            if (userId == null) {
                Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val donorData = hashMapOf(
                "userId" to userId,
                "fullName" to fullName,
                "gender" to gender,
                "dob" to dob,
                "email" to email,
                "bloodGroup" to bloodGroup,
                "location" to location
            )

            firestoreHelper.saveDonorRegistrationData(
                userId,
                donorData,
                onSuccess = {
                    // Optionally save role
                    firestoreHelper.saveUserRole(userId, "Donor", onSuccess = {}, onFailure = {})
                    Toast.makeText(this, "Donor Registered Successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                },
                onFailure = {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun showDatePickerDialog(onDateSet: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
            val date = "%02d/%02d/%04d".format(d, m + 1, y)
            onDateSet(date)
        }, year, month, day)

        datePickerDialog.show()
    }
}
