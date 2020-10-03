package com.demo.kotlinexamples.domain.model.contact

enum class RecordingType(val value: Int) {
    DEFAULT(-1),
    OFF(0),
    ON(1),

    ;

    companion object {
        fun from(value: Int?): RecordingType =
            values().firstOrNull { it.value == value } ?: OFF
    }
}
