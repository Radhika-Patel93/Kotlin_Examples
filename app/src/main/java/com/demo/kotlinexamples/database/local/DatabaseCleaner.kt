package com.demo.kotlinexamples.data.database.local

import androidx.room.RoomDatabase
import com.demo.kotlinexamples.domain.util.AppSchedulers
import com.demo.kotlinexamples.domain.util.Cleanable
import io.reactivex.Completable

class DatabaseCleaner(
    private val database: RoomDatabase
) : Cleanable {
    override fun clearData(): Completable {
        return Completable.fromAction { database.clearAllTables() }
            .subscribeOn(AppSchedulers.disk)
    }
}
