package com.php.ebloodconnect.donor

data class DonorRequest(
    val name: String,
    val bloodGroup: String,
    val unitsNeeded: Int,
    val contact: String
)