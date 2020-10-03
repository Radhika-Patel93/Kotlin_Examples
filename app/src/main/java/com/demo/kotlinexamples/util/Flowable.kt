package com.demo.kotlinexamples.util

import io.reactivex.Flowable
import io.reactivex.annotations.BackpressureKind
import io.reactivex.annotations.BackpressureSupport
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport

@CheckReturnValue
@BackpressureSupport(BackpressureKind.FULL)
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <T> Flowable<T>.onErrorComplete(crossinline predicate: (Throwable) -> Boolean = { true }): Flowable<T> =
    onErrorResumeNext { error: Throwable ->
        if (predicate(error)) Flowable.empty<T>() else Flowable.error<T>(error)
    }
