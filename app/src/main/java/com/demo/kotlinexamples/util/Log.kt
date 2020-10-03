package com.demo.kotlinexamples.util

import com.bumptech.glide.BuildConfig
import io.reactivex.*

object Log {
    interface Wrapper {
        fun v(tag: String, msg: String)
        fun v(tag: String, msg: String, tr: Throwable)

        fun d(tag: String, msg: String)
        fun d(tag: String, msg: String, tr: Throwable)

        fun i(tag: String, msg: String)
        fun i(tag: String, msg: String, tr: Throwable)

        fun w(tag: String, msg: String)
        fun w(tag: String, msg: String, tr: Throwable)
        fun w(tag: String, tr: Throwable)

        fun e(tag: String, msg: String)
        fun e(tag: String, msg: String, tr: Throwable)
    }

    private val VERBOSE: Boolean = BuildConfig.DEBUG

    var wrapper: Wrapper? = null

    fun v(tag: String, msg: String) = wrapper?.v(tag, msg)
    fun v(tag: String, msg: String, tr: Throwable) = wrapper?.v(tag, msg, tr)

    fun d(tag: String, msg: String) = wrapper?.d(tag, msg)
    fun d(tag: String, msg: String, tr: Throwable) = wrapper?.d(tag, msg, tr)

    fun i(tag: String, msg: String) = wrapper?.i(tag, msg)
    fun i(tag: String, msg: String, tr: Throwable) = wrapper?.i(tag, msg, tr)

    fun w(tag: String, msg: String) = wrapper?.w(tag, msg)
    fun w(tag: String, msg: String, tr: Throwable) = wrapper?.w(tag, msg, tr)
    fun w(tag: String, tr: Throwable) = wrapper?.w(tag, tr)

    fun e(tag: String, msg: String) = wrapper?.e(tag, msg)
    fun e(tag: String, msg: String, tr: Throwable) = wrapper?.e(tag, msg, tr)

     // Redact personally identifiable information for production users.
     // If we are running in verbose mode, return the original string,
     // and return "*" masked string otherwise.
    fun pii(obj: Any?): String = if (obj == null || VERBOSE) obj.toString() else "*".repeat(obj.toString().length)
}

inline val <reified T> T.TAG: String get() = T::class.java.simpleName

inline fun <reified T> T.logv(message: String) = Log.v(TAG, message)
inline fun <reified T> T.logv(message: String, tr: Throwable) = Log.v(TAG, message, tr)

inline fun <reified T> T.logd(message: String) = Log.d(TAG, message)
inline fun <reified T> T.logd(message: String, tr: Throwable) = Log.d(TAG, message, tr)

inline fun <reified T> T.logi(message: String) = Log.i(TAG, message)
inline fun <reified T> T.logi(message: String, tr: Throwable) = Log.i(TAG, message, tr)

inline fun <reified T> T.logw(message: String) = Log.w(TAG, message)
inline fun <reified T> T.logw(message: String, tr: Throwable) = Log.w(TAG, message, tr)
inline fun <reified T> T.logw(tr: Throwable) = Log.w(TAG, tr)

inline fun <reified T> T.loge(message: String) = Log.e(TAG, message)
inline fun <reified T> T.loge(message: String, tr: Throwable) = Log.e(TAG, message, tr)

inline fun <reified T, R> T.logExecTime(label: String? = null, block: () -> R): R {
    @Suppress("NAME_SHADOWING")
    val label = label?.takeIf { it.isNotEmpty() }?.plus(" - ") ?: ""

    logd("${label}start")

    val startTime = System.currentTimeMillis()
    val r = block()
    val endTime = System.currentTimeMillis()

    logd("${label}done, time = ${endTime - startTime} ms")

    return r
}

fun <T> Observable<T>.log(tag: String, msg: String): Observable<T> {
    return doOnSubscribe {  Log.d(tag, "$msg: start") }
        .doOnNext { Log.d(tag, "$msg: next") }
        .doOnComplete { Log.d(tag, "$msg: complete") }
        .doOnError { Log.w(tag, "$msg: error", it) }
}

fun <T> Flowable<T>.log(tag: String, msg: String): Flowable<T> {
    return doOnSubscribe {  Log.d(tag, "$msg: start") }
        .doOnNext { Log.d(tag, "$msg: next") }
        .doOnComplete { Log.d(tag, "$msg: complete") }
        .doOnError { Log.w(tag, "$msg: error", it) }
}

fun <T> Single<T>.log(tag: String, msg: String): Single<T> {
    return doOnSubscribe {  Log.d(tag, "$msg: start") }
        .doOnSuccess { Log.d(tag, "$msg: success") }
        .doOnError { Log.w(tag, "$msg: error", it) }
}

fun <T> Maybe<T>.log(tag: String, msg: String): Maybe<T> {
    return doOnSubscribe {  Log.d(tag, "$msg: start") }
        .doOnSuccess { Log.d(tag, "$msg: success") }
        .doOnComplete { Log.d(tag, "$msg: complete") }
        .doOnError { Log.w(tag, "$msg: error", it) }
}

fun Completable.log(tag: String, msg: String): Completable {
    return doOnSubscribe {  Log.d(tag, "$msg: start") }
        .doOnComplete { Log.d(tag, "$msg: complete") }
        .doOnError { Log.w(tag, "$msg: error", it) }
}
