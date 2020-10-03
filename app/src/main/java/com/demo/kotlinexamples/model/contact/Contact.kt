package com.demo.kotlinexamples.domain.model.contact

data class Contact(
    val id: Long = -1,
    val name: String = "",
    val firstName: String? = null,
    val lastName: String? = null,
    val phones: List<String> = listOf()
)

fun Contact.isExists(): Boolean = id >= 0
