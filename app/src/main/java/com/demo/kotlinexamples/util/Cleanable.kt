package com.demo.kotlinexamples.domain.util

import io.reactivex.Completable

interface Cleanable {
    fun clearData(): Completable
}
