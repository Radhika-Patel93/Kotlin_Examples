package com.demo.kotlinexamples.domain.util

import io.reactivex.Scheduler

object AppSchedulers {
    interface Wrapper {
        val main: Scheduler
        val ui: Scheduler
        val io: Scheduler
        val disk: Scheduler
        val network: Scheduler
        val computation: Scheduler
        val trampoline: Scheduler
        val newThread: Scheduler
    }

    lateinit var wrapper: Wrapper

    val main: Scheduler get() = wrapper.main
    val ui: Scheduler get() = wrapper.ui
    val io: Scheduler get() = wrapper.io
    val disk: Scheduler get() = wrapper.disk
    val network: Scheduler get() = wrapper.network
    val computation: Scheduler get() = wrapper.computation
    val trampoline: Scheduler get() = wrapper.trampoline
    val newThread: Scheduler get() = wrapper.newThread
}
