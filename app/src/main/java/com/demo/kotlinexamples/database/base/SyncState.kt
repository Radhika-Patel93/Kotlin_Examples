package com.demo.kotlinexamples.data.database.base

enum class SyncState(private val string: String) {
    PENDING("pending"),
    SYNCED("synced"),
    ;

    override fun toString() = string

    companion object {
        fun from(string: String?) =
            values().firstOrNull { it.string == string } ?: PENDING
    }
}
