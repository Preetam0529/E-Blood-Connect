package com.php.ebloodconnect.donor

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.php.ebloodconnect.FirestoreHelper
import com.php.ebloodconnect.R
import java.util.*

class DonorRegisterActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etDOB: EditText
    private lateinit var spinnerGender: Spinner
    private lateinit var spinnerBloodGroup: Spinner
    private lateinit var spinnerLocation: Spinner
    private lateinit var btnSubmit: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var firestoreHelper: FirestoreHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donorregister)

        auth = FirebaseAuth.getInstance()
        firestoreHelper = FirestoreHelper(this)

        // Bind views
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPass)
        etDOB = findViewById(R.id.etDOB)
        spinnerGender = findViewById(R.id.spinnerGender)
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup)
        spinnerLocation = findViewById(R.id.spinnerLocation)
        btnSubmit = findViewById(R.id.btn_submit2)

        setupSpinners()

        etDOB.setOnClickListener {
            showDatePickerDialog { etDOB.setText(it) }
        }

        btnSubmit.setOnClickListener {
            registerDonor()
        }
    }

    private fun setupSpinners() {
        spinnerGender.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            listOf("Select Gender", "Male", "Female", "Other")).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerBloodGroup.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            listOf("Select Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerLocation.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item,
            listOf("Select City", "Mumbai", "Delhi", "Bangalore", "Hyderabad", "Ahmedabad", "Chennai", "Kolkata", "Pune", "Jaipur", "Lucknow")).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun showDatePickerDialog(onDateSet: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, y, m, d ->
            onDateSet(String.format("%02d/%02d/%04d", d, m + 1, y))
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun registerDonor() {
        val name = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val dob = etDOB.text.toString().trim()
        val gender = spinnerGender.selectedItem.toString()
        val bloodGroup = spinnerBloodGroup.selectedItem.toString()
        val location = spinnerLocation.selectedItem.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || dob.isEmpty()
            || gender == "Select Gender" || bloodGroup == "Select Blood Group" || location == "Select City") {
            Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userId = auth.currentUser?.uid ?: return@addOnSuccessListener
                val donorData = hashMapOf(
                    "userId" to userId,
                    "fullName" to name,
                    "email" to email,
                    "dob" to dob,
                    "gender" to gender,
                    "bloodGroup" to bloodGroup,
                    "location" to location
                )

                firestoreHelper.saveDonorRegistrationData(
                    userId,
                    donorData,
                    onSuccess = {
                        Toast.makeText(this, "Donor registered successfully!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, DonorMainActivity::class.java))
                        finish()
                    },
                    onFailure = {
                        Toast.makeText(this, "Firestore error: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            .addOnFailureListener {
                Toast.makeText(this, "Firebase Auth error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
