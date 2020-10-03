package com.demo.kotlinexamples.data.database.remote

import com.demo.kotlinexamples.data.database.remote.dao.*

interface PhoneicRemoteDatabase {

    fun numberListsDao(): RemoteNumberListsDao

    fun bookListsDao(): RemoteBookListsDao

}
