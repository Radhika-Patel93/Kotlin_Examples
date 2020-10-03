package com.demo.kotlinexamples.util

class Optional<M> private constructor(private val value: M? = null) {
    companion object {
        private val EMPTY = Optional<Any>()

        fun <T> empty(): Optional<T> = EMPTY as Optional<T>

        fun <T> of(obj: T?): Optional<T> = Optional(obj)
    }

    val isPresent get() = value != null

    fun get(): M = value!!

    fun orElse(other: M?): M? = value ?: other
}
