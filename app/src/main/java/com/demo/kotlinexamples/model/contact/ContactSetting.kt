package com.demo.kotlinexamples.domain.model.contact

data class ContactSetting(
    val uid: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val id: String = "",
    val phone: String = "",
    val deviceId: String = "",
    val company: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val recordingType: RecordingType = RecordingType.DEFAULT
)
