package com.php.ebloodconnect.acceptor

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.php.ebloodconnect.FirestoreHelper
import com.php.ebloodconnect.R

class AcceptorRegisterActivity : AppCompatActivity() {

    private lateinit var etHospitalName: EditText
    private lateinit var etAdminName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etContactNumber: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnUploadID: Button
    private lateinit var btnSubmit: Button
    private lateinit var spinnerLocation: Spinner

    private var idProofUri: Uri? = null
    private var selectedLat = 0.0
    private var selectedLon = 0.0
    private var selectedLocationName = ""

    private val ID_PROOF_PICKER_REQUEST = 102

    private val savedLocations = listOf(
        "Select Location",
        "Apollo Hospital, Delhi",
        "Fortis, Mumbai",
        "AIIMS, Delhi",
        "Manipal Hospital, Bangalore"
    )

    private val locationCoords = mapOf(
        "Apollo Hospital, Delhi" to Pair(28.5672, 77.2100),
        "Fortis, Mumbai" to Pair(19.1076, 72.8374),
        "AIIMS, Delhi" to Pair(28.5665, 77.2100),
        "Manipal Hospital, Bangalore" to Pair(12.9436, 77.5968)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceptor_register)

        etHospitalName = findViewById(R.id.et_hospital_name)
        etAdminName = findViewById(R.id.et_admin_person)
        etEmail = findViewById(R.id.et_email)
        etContactNumber = findViewById(R.id.et_contact_number)
        etPassword = findViewById(R.id.Pass1)
        etConfirmPassword = findViewById(R.id.pass2)
        btnUploadID = findViewById(R.id.btn_upload_id)
        btnSubmit = findViewById(R.id.btn_submit)
        spinnerLocation = findViewById(R.id.spinner_location)

        // Set up Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, savedLocations)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLocation.adapter = adapter

        spinnerLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val locationName = savedLocations[position]
                if (locationCoords.containsKey(locationName)) {
                    selectedLocationName = locationName
                    val coords = locationCoords[locationName]
                    selectedLat = coords?.first ?: 0.0
                    selectedLon = coords?.second ?: 0.0
                } else {
                    selectedLocationName = ""
                    selectedLat = 0.0
                    selectedLon = 0.0
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnUploadID.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf"
            startActivityForResult(intent, ID_PROOF_PICKER_REQUEST)
        }

        btnSubmit.setOnClickListener {
            registerAcceptor()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ID_PROOF_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            idProofUri = data?.data
            Toast.makeText(this, "ID Proof Selected ✅", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registerAcceptor() {
        val hospitalName = etHospitalName.text.toString().trim()
        val adminName = etAdminName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etContactNumber.text.toString().trim()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if (hospitalName.isEmpty() || adminName.isEmpty() || email.isEmpty() ||
            phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
            idProofUri == null || selectedLocationName.isEmpty()
        ) {
            Toast.makeText(this, "Please fill all fields, upload ID proof & select a location", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val userId = authResult.user?.uid ?: return@addOnSuccessListener

                val storageRef = FirebaseStorage.getInstance().reference
                    .child("id_proofs/$userId.pdf")

                storageRef.putFile(idProofUri!!)
                    .addOnSuccessListener {
                        storageRef.downloadUrl.addOnSuccessListener { uri ->
                            FirestoreHelper(requireContext()).saveAcceptorRegistrationData(
                                userId,
                                hospitalName,
                                adminName,
                                email,
                                phone,
                                uri.toString(),
                                selectedLat,
                                selectedLon,
                                selectedLocationName,
                                onSuccess = {
                                    Toast.makeText(this, "Registration successful ✅", Toast.LENGTH_SHORT).show()
                                    finish()
                                },
                                onFailure = {
                                    Toast.makeText(this, "Firestore Error: ${it.message}", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to upload ID proof", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Registration failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
