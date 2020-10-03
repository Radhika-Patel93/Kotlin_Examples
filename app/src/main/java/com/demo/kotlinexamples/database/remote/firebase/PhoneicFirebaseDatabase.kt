package com.demo.kotlinexamples.data.database.remote.firebase

import com.demo.kotlinexamples.util.logi
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.demo.kotlinexamples.data.database.remote.PhoneicRemoteDatabase
import com.demo.kotlinexamples.data.database.remote.dao.*
import com.demo.kotlinexamples.data.database.remote.firebase.numberlists.FbBookListsDao
import com.demo.kotlinexamples.data.database.remote.firebase.numberlists.FbNumberListsDao
import io.reactivex.disposables.Disposable

class PhoneicFirebaseDatabase(
    private val database: FirebaseDatabase,
) : PhoneicRemoteDatabase {


    // Root paths
    val rootRef: DatabaseReference get() = database.reference
    val numberLists: DatabaseReference get() = rootRef.child("/phone/number_lists")
    companion object {
//    val bookLists: DatabaseReference get() = rootRef.child("/store/book")
    val bookLists: DatabaseReference get() = FirebaseDatabase.getInstance().reference.child("/store/book")
    }

    // User paths

    val userNumberLists: DatabaseReference get() = require(userUid.isNotBlank()).let { numberLists.child(userUid) }
    val userNumberListsNumbers: DatabaseReference get() = userNumberLists.child("/numbers")


    private var userUid: String = ""

    override fun numberListsDao(): RemoteNumberListsDao = FbNumberListsDao(this)

    override fun bookListsDao(): RemoteBookListsDao = FbBookListsDao(this)

}
