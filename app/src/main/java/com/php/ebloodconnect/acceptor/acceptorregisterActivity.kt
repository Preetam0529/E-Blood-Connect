package com.php.ebloodconnect.acceptor

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.php.blooddonate.databinding.ActivityAcceptorRegistrationBinding

class acceptorregisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAcceptorRegistrationBinding


    private var idProofUri: Uri? = null

    companion object {
        const val ID_PROOF_REQUEST_CODE = 101
        const val LOCATION_REQUEST_CODE = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAcceptorRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Upload ID Proof
        binding.btnUploadId.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "application/pdf" // or "image/*" if you want images
            startActivityForResult(Intent.createChooser(intent, "Select ID Proof"), ID_PROOF_REQUEST_CODE)
        }

        // Select Location (can be linked to a map or location picker)
        binding.btnSelectLocation.setOnClickListener {
            Toast.makeText(this, "Location picker not implemented yet", Toast.LENGTH_SHORT).show()
            // You can launch a map intent or use FusedLocationProviderClient here
        }

        // Submit Form
        binding.btnSubmit.setOnClickListener {
            val hospitalName = binding.etHospitalName.text.toString().trim()
            val adminName = binding.etAdminPerson.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val contact = binding.etContactNumber.text.toString().trim()

            if (hospitalName.isEmpty() || adminName.isEmpty() || email.isEmpty() || contact.isEmpty() || idProofUri == null) {
                Toast.makeText(this, "Please fill all fields and upload ID proof", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Submit logic here (e.g., upload to Firebase or store in database)
            Toast.makeText(this, "Registration Submitted!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ID_PROOF_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            idProofUri = data?.data
            Toast.makeText(this, "ID Proof Uploaded", Toast.LENGTH_SHORT).show()
        }
    }
}
