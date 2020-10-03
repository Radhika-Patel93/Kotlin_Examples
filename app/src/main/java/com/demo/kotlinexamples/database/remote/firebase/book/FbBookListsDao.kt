package com.demo.kotlinexamples.data.database.remote.firebase.numberlists

import com.androidhuman.rxfirebase2.database.ChildEvent
import com.androidhuman.rxfirebase2.database.rxUpdateChildren
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.demo.kotlinexamples.data.database.base.EntityEvent
import com.demo.kotlinexamples.data.database.common.entity.BookListsEntity
import com.demo.kotlinexamples.data.database.remote.dao.RemoteBookListsDao
import com.demo.kotlinexamples.data.database.remote.firebase.PhoneicFirebaseDatabase
import com.demo.kotlinexamples.data.database.remote.firebase.base.FbBaseDao
import com.demo.kotlinexamples.data.database.remote.firebase.base.FbMap
import io.reactivex.Completable

class FbBookListsDao(
    val firebaseDatabase: PhoneicFirebaseDatabase
) : FbBaseDao<BookListsEntity>(), RemoteBookListsDao {

    //this.getAll(Filter().add(FbNumberLists.lastUpdated, Filter.Type.GREATER_THAN_OR_EQUAL, ""))

    public override fun getAllRef(): DatabaseReference {
        return PhoneicFirebaseDatabase.bookLists
    }

    override fun getPagingOrderByQuery(): Query {
        return getAllRef().orderByKey()
    }

    fun insertOrUpdate(data : Map<String,Any>): Completable {
        return getAllRef().rxUpdateChildren(data)
    }

    override fun entityToMap(entity: BookListsEntity): FbMap {
        return FbBookListsConverter.entityToMap(entity)
    }

    override fun snapshotToEntity(dataSnapshot: DataSnapshot): BookListsEntity {
        return FbBookListsConverter.snapshotToEntity(dataSnapshot)
    }

    override fun childEventToEntityEvent(childEvent: ChildEvent): EntityEvent {
        return FbBookListsConverter.childEventToEntityEvent(childEvent)
    }

    override fun filterKeyToFirebaseKey(key: String): String {
        return when (key) {
            BookListsEntity.PRIMARY_KEY -> FbBookLists.key
            else -> super.filterKeyToFirebaseKey(key)
        }
    }
}
