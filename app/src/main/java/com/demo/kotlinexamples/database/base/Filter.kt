package com.demo.kotlinexamples.data.database.base

import java.util.*

/**
 * A filter is a map of keys with a filter type and a value
 */
class Filter : WeakHashMap<String, Filter.TypeValuePair<*>>() {
    enum class Type {
        EQUAL,
        @Deprecated("Unsupported by Firebase. Please don't use it, or find way how to implement it for Firebase.")
        NOT_EQUAL,
        @Deprecated("Unsupported by Firebase. Please don't use it, or find way how to implement it for Firebase.")
        GREATER_THAN,
        @Deprecated("Unsupported by Firebase. Please don't use it, or find way how to implement it for Firebase.")
        LESS_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN_OR_EQUAL
    }

    fun <T> add(key: String, value: T): Filter {
        this[key] = TypeValuePair(
            Type.EQUAL,
            value
        )
        return this
    }

    fun <T> add(key: String, type: Type, value: T): Filter {
        this[key] = TypeValuePair(type, value)
        return this
    }

    fun <T> add(key: String, type: Type, values: List<T>): Filter {
        for (value in values) {
            this[key] = TypeValuePair(type, value)
        }
        return this
    }

    fun <T> add(key: String, type: Type, values: Array<T>): Filter {
        for (value in values) {
            this[key] = TypeValuePair(type, value)
        }
        return this
    }

    data class TypeValuePair<T>(val filterType: Type, val value: T)
}
