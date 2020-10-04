package com.demo.kotlinexamples.firebase

import android.content.Context
import androidx.room.Room.databaseBuilder
import com.demo.kotlinexamples.data.database.common.entity.BookListsEntity
import com.demo.kotlinexamples.data.database.local.PhoneicCacheDatabase
import com.demo.kotlinexamples.data.database.remote.firebase.PhoneicFirebaseDatabase
import com.demo.kotlinexamples.data.database.remote.firebase.base.toFbMap
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object Realtime_Database {

    fun getBookList(context: Context){

        var dbMainRef = PhoneicFirebaseDatabase.bookLists
        dbMainRef.ref.addValueEventListener( object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                children.forEachIndexed { index, it ->
                    println("Key : " + it.key)
                    println("Value : " + it.value)

                    initDatabase(context)
                    saveDataInDatabase(context, index, it.key.toString(), Gson().toJson(it.value))
                }
                  fetchAllData(context)
            }

        })

    }

//    Database functions;
    fun initDatabase(context: Context): PhoneicCacheDatabase {
        val db = databaseBuilder(context, PhoneicCacheDatabase::class.java, "todo.db").build()
        return db
    }

    fun saveDataInDatabase(context: Context, id: Int, key: String, value: String) {
        val db = initDatabase(context)
        val thread = Thread {
            var todoEntity = BookListsEntity(id.toString(), 0,0,key, value)
            db.bookListsDao().insertData(todoEntity)
        }
        thread.start()
    }

    fun fetchAllData(context: Context) {
        val thread = Thread {
            val db = initDatabase(context)
            //fetch Records
            db.bookListsDao().getData().forEach()
            {
               StringToJson(Gson().fromJson(it.rawData, object : TypeToken<Map<String, Any>>() {}.type))
            }
        }
        thread.start()

    }

    fun StringToJson(fromJson: Any) {

        fromJson.toFbMap()?.forEach(){
            println("Fetch Key: "+ it.key)
            println("Fetch Value: "+ it.value)
        }
    }

}


