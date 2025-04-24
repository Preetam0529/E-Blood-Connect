package com.php.ebloodconnect.donor

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.php.ebloodconnect.FirestoreHelper
import com.php.ebloodconnect.MainActivity
import com.php.ebloodconnect.R
import com.php.ebloodconnect.UserAuthentication


class DonorProfile : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private lateinit var logoutButton: Button
    private lateinit var firestoreHelper: FirestoreHelper
    private lateinit var auth: FirebaseAuth
    private val PICK_IMAGE_REQUEST = 1001
    private var imageUri: Uri? = null
    private val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donorprofile)

        profileImage = findViewById(R.id.imageProfile)
        editButton = findViewById(R.id.buttonEditDetails)
        deleteButton = findViewById(R.id.buttonDeleteAccount)
        logoutButton = findViewById(R.id.buttonLogout)

        auth = FirebaseAuth.getInstance()
        firestoreHelper = FirestoreHelper(this)

        profileImage.setOnClickListener {
            openGallery()
        }

        editButton.setOnClickListener {
            val intent = Intent(this, DonorRegisterActivity::class.java)
            startActivity(intent)
        }

        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, UserAuthentication::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            profileImage.setImageURI(imageUri)
            uploadImageToFirebase(imageUri)
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri?) {
        val user = auth.currentUser ?: return
        val userId = user.uid
        val imageRef = storageRef.child("profile_images/$userId.jpg")

        imageUri?.let {
            imageRef.putFile(it)
                .addOnSuccessListener {
                    Toast.makeText(this, "Profile image uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Image upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to permanently delete your account?")
            .setPositiveButton("Yes") { _, _ ->
                deleteAccount()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteAccount() {
        val user = auth.currentUser
        val userId = user?.uid ?: return

        firestoreHelper.deleteDonorData(userId,
            onSuccess = {
                user.delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Account deleted", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, DonorLoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to delete user from Auth", Toast.LENGTH_SHORT).show()
                    }
            },
            onFailure = {
                Toast.makeText(this, "Failed to delete donor data: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
