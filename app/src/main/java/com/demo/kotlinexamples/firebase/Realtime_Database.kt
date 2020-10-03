package com.demo.kotlinexamples.firebase

import android.content.Context
import androidx.room.Room.databaseBuilder
import com.demo.kotlinexamples.data.database.remote.firebase.PhoneicFirebaseDatabase
import com.demo.kotlinexamples.data.database.remote.firebase.numberlists.FbBookLists
import com.demo.kotlinexamples.data.database.remote.firebase.numberlists.FbBookListsDao
import com.demo.kotlinexamples.util.logd
import com.google.firebase.database.*
import com.google.gson.Gson
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable


object Realtime_Database {

    fun getBookList(){

        var dbMainRef = PhoneicFirebaseDatabase.bookLists
        dbMainRef.ref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                children.forEachIndexed { index, it ->
                    println("Key : " + it.key)
                    println("Value : " + it.value)
                }

            }

        })

    }

}


//    fun getAllData(context: Context) {
//        val rootRef = FirebaseDatabase.getInstance().reference.child("/stagging/data")
//        rootRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val children = snapshot.children
//                children.forEachIndexed { index, it ->
//                    println("Key : " + it.key)
//                    println("Value : " + it.value)
//
//                    initDatabase(context)
//                    saveDataInDatabase(context, index, it.key.toString(), Gson().toJson(it.value))
////                    StringToJson(Gson().toJson(it.value))
//                }
//
//                fetchAllData(context)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                println(error.message)
//            }
//        })
//    }
//
//    //Database functions;
//    fun initDatabase(context: Context): AppDatabase {
//        val db = databaseBuilder(context, AppDatabase::class.java, "todo.db").build()
//        return db
//    }
//
//    fun saveDataInDatabase(context: Context, id: Int, key: String, value: String) {
//        val db = initDatabase(context)
//        val thread = Thread {
//            var todoEntity = TodoEntity(id, key, value)
//            db.todoDao().insertAll(todoEntity)
//        }
//        thread.start()
//    }
//
//    fun fetchAllData(context: Context) {
//        val thread = Thread {
//            val db = initDatabase(context)
//            //fetch Records
//            db.todoDao().getAll().forEach()
//            {
//                println("Fetch Records Id:  : ${it.id}")
//                println("Fetch Records Key:  : ${it.key}")
//                println("Fetch Records Value:  : ${it.value}")
//            }
//
//            println("Fetch Records:  : ${db.todoDao().getAll().size}")
//        }
//        thread.start()
//
//    }

//    fun StringToJson(data: String) {
//        val gson = Gson()
//        val mapType = object : TypeToken<Map<String, Any>>() {}.type
//
//        var tutorialMap: Map<String, Any> = gson.fromJson(data, object : TypeToken<Map<String, Any>>() {}.type)
//        tutorialMap.forEach {
//            println("StringToJson: "+it.key+"  ||  "+it.value)
//        }
//    }


