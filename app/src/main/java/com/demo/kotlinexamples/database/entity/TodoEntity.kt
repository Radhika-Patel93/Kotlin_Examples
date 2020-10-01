package com.demo.kotlinexamples.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.JsonObject
import org.json.JSONObject

@Entity
data class TodoEntity(@PrimaryKey val id: Int, @ColumnInfo(name = "Key") val key: String, @ColumnInfo(name = "Value") val value: String)