package com.php.ebloodconnect

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.GeoPoint

class FirestoreHelper(requireContext: Context) {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // =============================
    // User Role (Donor or Acceptor)
    // =============================

    fun saveUserRole(userId: String, role: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val userRef = db.collection("users").document(userId)
        val userData = hashMapOf("role" to role)

        userRef.set(userData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun getUserRole(userId: String, onResult: (String?) -> Unit, onFailure: (Exception) -> Unit) {
        val userRef = db.collection("users").document(userId)

        userRef.get()
            .addOnSuccessListener { document ->
                val role = document.getString("role")
                onResult(role)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    // =============================
    // Save Acceptor Registration Info
    // =============================

    fun saveAcceptorRegistrationData(
        userId: String,
        hospitalName: String,
        adminName: String,
        email: String,
        contact: String,
        idProofUrl: String,
        latitude: Double,
        longitude: Double,
        locationName: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val acceptorData = hashMapOf(
            "userId" to userId,
            "hospitalName" to hospitalName,
            "adminName" to adminName,
            "email" to email,
            "contact" to contact,
            "idProofUrl" to idProofUrl,
            "location" to GeoPoint(latitude, longitude),
            "locationName" to locationName
        )

        db.collection("acceptors")
            .document(userId)
            .set(acceptorData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun saveDonorRegistrationData(
        userId: String,
        donorData: HashMap<String, String>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("donors")
            .document(userId)
            .set(donorData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    // =============================
    // Posting Requests by Acceptors
    // =============================

    fun postAcceptorRequest(
        requestData: HashMap<String, Any>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("donation_requests")
            .add(requestData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    // =============================
    // Acceptor Feed - See Donors Who Accepted
    // =============================

    fun getAcceptedRequestsForAcceptor(
        userId: String,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("donation_requests")
            .whereEqualTo("userId", userId)
            .whereEqualTo("status", "Accepted")
            .get()
            .addOnSuccessListener { result -> onSuccess(result) }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun getAllBloodRequests(
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("bloodRequests")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun updateDonorConfirmationStatus(
        requestId: String,
        donorId: String,
        status: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val updateData = mapOf("status" to status)

        db.collection("donation_requests")
            .document(requestId)
            .collection("donorConfirmations")
            .document(donorId)
            .update(updateData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }


    fun deleteDonorData(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("donors")
            .document(userId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun deleteAcceptorAccount(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("acceptors")
            .document(userId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }


    fun getConfirmedDonors(
        requestId: String,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("bloodRequests")
            .document(requestId)
            .collection("donorConfirmations")
            .get()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }

    fun acceptDonationRequest(
        requestId: String,
        donorId: String,
        scheduledDateTime: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val data = mapOf(
            "status" to "Accepted",
            "scheduledDateTime" to scheduledDateTime,
            "approvedDonorId" to donorId
        )

        db.collection("bloodRequests")
            .document(requestId)
            .update(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun declineDonationRequest(
        requestId: String,
        donorId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val data = mapOf(
            "status" to "Declined",
            "declinedDonorId" to donorId
        )

        db.collection("bloodRequests")
            .document(requestId)
            .update(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }


    fun confirmDonation(
        requestId: String,
        donorId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val confirmation = hashMapOf(
            "donorId" to donorId,
            "confirmedAt" to System.currentTimeMillis()
        )

        db.collection("bloodRequests")
            .document(requestId)
            .collection("donorConfirmations")
            .document(donorId)
            .set(confirmation)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getDonorDetails(
        donorId: String,
        onSuccess: (Map<String, Any>?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("donors")
            .document(donorId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    onSuccess(document.data)
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { e -> onFailure(e) }
    }


//    fun getConfirmedDonorsForRequest(
//        requestId: String,
//        callback: (List<DonorConfirmation>) -> Unit
//    ) {
//        db.collection("donorConfirmations")
//            .whereEqualTo("requestId", requestId)
//            .whereEqualTo("status", "confirmed")
//            .get()
//            .addOnSuccessListener { snapshot ->
//                val list = snapshot.documents.mapNotNull {
//                    it.toObject(DonorConfirmation::class.java)?.apply {
//                        id = it.id
//                    }
//                }
//                callback(list)
//            }
//            .addOnFailureListener {
//                callback(emptyList())
//            }
//    }

    // =============================
    // Get All Requests for an Acceptor (any status)
    // =============================

    fun getAllRequestsForAcceptor(
        userId: String,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("donation_requests")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result -> onSuccess(result) }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // =============================
    // Update Request Status
    // =============================

    fun updateRequestStatus(
        requestId: String,
        newStatus: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("donation_requests")
            .document(requestId)
            .update("status", newStatus)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    // =============================
    // Donor Confirmation for Accepting/Declining Donation
    // =============================

    // Save donor confirmation for the acceptor's request
    fun saveDonorConfirmation(
        requestId: String,
        donorId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val donorConfirmationData = hashMapOf(
            "donorId" to donorId,
            "confirmedAt" to System.currentTimeMillis()
        )

        db.collection("donation_requests")
            .document(requestId)
            .collection("donorConfirmations")
            .document(donorId)
            .set(donorConfirmationData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun markDonationFulfilled(
        requestId: String,
        donorId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val data = mapOf("status" to "Fulfilled")

        db.collection("donation_requests")
            .document(requestId)
            .collection("donorConfirmations")
            .document(donorId)
            .update(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun confirmDonationToRequest(
        requestId: String,
        donorId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val confirmation = hashMapOf(
            "donorId" to donorId,
            "confirmedAt" to System.currentTimeMillis()
        )

        db.collection("donation_requests")
            .document(requestId)
            .collection("donorConfirmations")
            .document(donorId)
            .set(confirmation)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }



    // Fetch the list of confirmed donors for an acceptor's request
    fun getDonorConfirmationsForRequest(
        requestId: String,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("donation_requests")
            .document(requestId)
            .collection("donorConfirmations")
            .get()
            .addOnSuccessListener { result -> onSuccess(result) }
            .addOnFailureListener { exception -> onFailure(exception) }
    }
}
