package com.demo.kotlinexamples.data.database.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.demo.kotlinexamples.data.database.common.entity.*
import com.demo.kotlinexamples.data.database.local.dao.*
import com.demo.kotlinexamples.database.local.dao.BookListsDao

@Database(
    entities = [
        NumberListsEntity::class,
        BookListsEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(DatabaseTypeConverters::class)
abstract class PhoneicCacheDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "phoneic_cache.db"
    }

    abstract fun numberListsDao(): NumberListsDao
    abstract fun bookListsDao(): BookListsDao
}
