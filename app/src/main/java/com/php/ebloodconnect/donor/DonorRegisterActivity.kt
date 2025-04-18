package com.php.ebloodconnect.donor

import com.php.ebloodconnect.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class DonorRegisterActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var spinnerGender: Spinner
    private lateinit var etDOB: EditText
    private lateinit var etEmail: EditText
    private lateinit var spinnerBloodGroup: Spinner
    private lateinit var etLastDonation: EditText
    private lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donorregister)

        // Binding views
        etFullName = findViewById(R.id.etFullName)
        spinnerGender = findViewById(R.id.spinnerGender)
        etDOB = findViewById(R.id.etDOB)
        etEmail = findViewById(R.id.etEmail)
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup)
        etLastDonation = findViewById(R.id.etLastDonation)
        btnSubmit = findViewById(R.id.btn_submit2)

        // Spinner: Gender
        val genderOptions = listOf("Select Gender", "Male", "Female", "Other")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderOptions)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter = genderAdapter

        // Spinner: Blood Group
        val bloodGroups = listOf("Select Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val bloodGroupAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bloodGroups)
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBloodGroup.adapter = bloodGroupAdapter

        // Show DatePicker for DOB
        etDOB.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                etDOB.setText(selectedDate)
            }
        }

        // Show DatePicker for Last Donation Date
        etLastDonation.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                etLastDonation.setText(selectedDate)
            }
        }

        // Submit Button Click Listener
        btnSubmit.setOnClickListener {
            val fullName = etFullName.text.toString()
            val gender = spinnerGender.selectedItem.toString()
            val dob = etDOB.text.toString()
            val email = etEmail.text.toString()
            val bloodGroup = spinnerBloodGroup.selectedItem.toString()
            val lastDonation = etLastDonation.text.toString()

            // Handle submission here
            Toast.makeText(this, "Submitted Successfully!", Toast.LENGTH_SHORT).show()

            // Optionally send to Firebase or SQLite
        }
    }

    // Helper function for DatePickerDialog
    private fun showDatePickerDialog(onDateSet: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = "%02d/%02d/%04d".format(selectedDay, selectedMonth + 1, selectedYear)
            onDateSet(date)
        }, year, month, day)

        datePickerDialog.show()
    }
}
