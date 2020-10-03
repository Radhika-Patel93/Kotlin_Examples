package com.demo.kotlinexamples.domain.model.contact

data class ContactCreateData(
    val contactNumber: String = "",
    val newFirstName: String = "",
    val newLastName: String = "",
    val newCompanyName: String = "",
    val newNumber: String = "",
    val newNumberType: String = "",
    val newImage: String = "",
    val blockUntilSuccess: Boolean = false,
    val unblockTimeoutSec: Long = 0L,
    val createOnlyIfNotExists: Boolean = false,
    val editIfExists: Boolean = false,
    val temp: Boolean = false
)
