package com.demo.kotlinexamples.data.database.base

interface BaseEntity {
    companion object {
        const val CREATED_AT_KEY = "created_at"
        const val UPDATED_AT_KEY = "updated_at"
    }

    data class UidDesc(val name: String, val value: String)

    val uidDesc: UidDesc
}
