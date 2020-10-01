package com.demo.kotlinexamples.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.demo.kotlinexamples.database.dao.TodoDAO
import com.demo.kotlinexamples.database.entity.TodoEntity

@Database(entities = [(TodoEntity::class)], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDAO
}