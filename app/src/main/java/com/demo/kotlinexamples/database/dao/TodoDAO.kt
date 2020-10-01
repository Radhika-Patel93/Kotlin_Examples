package com.demo.kotlinexamples.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.demo.kotlinexamples.database.entity.TodoEntity

@Dao
interface TodoDAO {

    @Query("SELECT * FROM TodoEntity")
    fun getAll(): List<TodoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: TodoEntity)

}