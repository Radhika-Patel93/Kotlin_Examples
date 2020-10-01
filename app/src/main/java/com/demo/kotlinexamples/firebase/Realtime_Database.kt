package com.demo.kotlinexamples.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


object Realtime_Database {

    fun getAllData(){
        val rootRef = FirebaseDatabase.getInstance().reference.child("/stagging/data")
        rootRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot!!.children
                children.forEach {
                    println("Key : "+it.key)
                    println("Value : "+it.value)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println(error!!.message)
            }
        })
    }
}