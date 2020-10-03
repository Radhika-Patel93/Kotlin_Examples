package com.demo.kotlinexamples.util

fun unsupportedOperation(message: String = "This method is not implemented in the child class"): Nothing =
    throw UnsupportedOperationException(message)
