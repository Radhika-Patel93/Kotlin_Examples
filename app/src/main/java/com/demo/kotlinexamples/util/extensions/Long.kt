package com.demo.kotlinexamples.domain.extensions

fun Long.asTimeMillis(default: Long = System.currentTimeMillis()): Long =
    when {
        this <= 0L -> default // unknown, return default
        this <= Int.MAX_VALUE -> this * 1000 // in seconds, convert to milliseconds
        else -> this
    }
