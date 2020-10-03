package com.demo.kotlinexamples.domain.base

import com.demo.kotlinexamples.util.Log
import java.util.*

/**
 * Returns '1' if this is: 'true', otherwise '0'
 */
fun Boolean?.toInt(): Int = if (this == true) 1 else 0

/**
 * Returns '1L' if this is: 'true', otherwise '0L'
 */
fun Boolean?.toLong(): Long = if (this == true) 1L else 0L

/**
 * Returns '1.0' if this is: 'true', otherwise '0.0'
 */
fun Boolean?.toDouble(): Double = if (this == true) 1.0 else 0.0

/**
 * Returns 'false' if this is: 'null' or '0', otherwise 'true'
 */
fun Int?.toBoolean(): Boolean = this != null && this != 0

/**
 * Returns 'false' if this is: 'null' or '0L', otherwise 'true'
 */
fun Long?.toBoolean(): Boolean = this != null && this != 0L

/**
 * Returns 'false' if this is: 'null' or '0.0', otherwise 'true'
 */
fun Double?.toBoolean(): Boolean = this != null && this != 0.0

/**
 * Returns 'false' if this is: 'null', 'false', '0', '0L', '0.0', "", " ", "false", "FALSE", "no", "NO", "0", "0.0"
 *          otherwise 'true'
 */
fun Any?.toSrvBoolean(): Boolean = when(this) {
    is Boolean -> this
    is Int -> this.toBoolean()
    is Long -> this.toBoolean()
    is Double -> this.toBoolean()
    is String -> this.toSrvBoolean()
    else -> false
}

/**
 * Returns '0' if this is: 'null', 'false', '0', '0L', '0.0', "", " ", "false", "FALSE", "no", "NO", "0", "0.0"
 *          otherwise actual Int value
 */
fun Any?.toSrvInt(): Int = when(this) {
    is Boolean -> this.toInt()
    is Int -> this
    is Long -> this.toInt()
    is Double -> this.toInt()
    is String -> this.toSrvInt()
    else -> 0
}

/**
 * Returns '0L' if this is: 'null', 'false', '0', '0L', '0.0', "", " ", "false", "FALSE", "no", "NO", "0", "0.0"
 *          otherwise actual Long value
 */
fun Any?.toSrvLong(): Long = when(this) {
    is Boolean -> this.toLong()
    is Int -> this.toLong()
    is Long -> this
    is Double -> this.toLong()
    is String -> this.toSrvLong()
    else -> 0L
}

/**
 * Returns '0.0' if this is: 'null', 'false', '0', '0L', '0.0', "", " ", "false", "FALSE", "no", "NO", "0", "0.0"
 *          otherwise actual Double value
 */
fun Any?.toSrvDouble(): Double = when(this) {
    is Boolean -> this.toDouble()
    is Int -> this.toDouble()
    is Long -> this.toDouble()
    is Double -> this
    is String -> this.toSrvDouble()
    else -> 0.0
}

/**
 * Returns empty string if this is: 'null', otherwise actual String value
 */
fun Any?.toSrvString(): String = this?.toString().orEmpty()

/**
 * Set of negative values, what should be interpreted as logical 'false' or '0'
 * Note1: all string values is case insensitive here, so "FALSE", "False", "NO", "No" is also valid for this set.
 * Note2: empty "" string also meaning blank strings, so " ", "<any number of whitespaces>" is also valid for this set.
 */
private val negative: Set<Any?> = setOf(null, false, 0, 0L, 0.0, "", "false", "no", "0", "0.0") // strings should be in lower case

/**
 * Set of positive values, what should be interpreted as logical 'true' or '1'
 * Note: all string values is case insensitive here, so "TRUE”, "True", "YES", "Yes" is also valid for this set.
 */
private val positive: Set<Any?> = setOf(true, 1, 1L, 1.0, "true", "yes", "1", "1.0") // strings should be in lower case

/**
 * Returns 'false' if this string has one of the value from negative set, otherwise 'true'
 */
private fun String?.toSrvBoolean(): Boolean = negative.contains(this?.trim()?.toLowerCase()).not()

/**
 * Returns '0' if this string has one of the value from negative set or if it is not a number,
 *         '1' if this string has one of the value from positive set,
 *         otherwise actual Int value
 */
private fun String?.toSrvInt(): Int = toSrvDouble().toInt()

/**
 * Returns '0L' if this string has one of the value from negative set or if it is not a number,
 *         '1L' if this string has one of the value from positive set,
 *         otherwise actual Long value
 */
private fun String?.toSrvLong(): Long = toSrvDouble().toLong()

/**
 * @return '0.0' if this string has one of the value from negative set or if it is not a number,
 *         '1.0' if this string has one of the value from positive set,
 *         otherwise actual Double value
 */
private fun String?.toSrvDouble(defaultValue: Double = 0.0): Double {
    if (negative.contains(this?.trim()?.toLowerCase(Locale.ROOT))) return 0.0
    if (positive.contains(this?.trim()?.toLowerCase(Locale.ROOT))) return 1.0
    return try {
        java.lang.Double.parseDouble(this!!) // let it throw if its null
    } catch (e: Exception) {
        Log.e("Primitives", "Unable to convert the value=$this to Double, return defaultValue=$defaultValue", e)
        defaultValue
    }
}

