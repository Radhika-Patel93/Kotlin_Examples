package com.demo.kotlinexamples.domain.model.contact

data class ContactRecording(
    val id: Long,
    val name: String,
    val firstName: String?,
    val lastName: String?,
    val phones: List<String>,
    val recordingType: RecordingType,
    val company: String = "",
    val email: String = "",
    val settingId: String = ""
)