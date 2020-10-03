package com.demo.kotlinexamples.data.database.remote.firebase.numberlists

import com.androidhuman.rxfirebase2.database.ChildEvent
import com.androidhuman.rxfirebase2.database.rxUpdateChildren
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.demo.kotlinexamples.data.database.base.EntityEvent
import com.demo.kotlinexamples.data.database.common.entity.NumberListsEntity
import com.demo.kotlinexamples.data.database.remote.dao.RemoteNumberListsDao
import com.demo.kotlinexamples.data.database.remote.firebase.PhoneicFirebaseDatabase
import com.demo.kotlinexamples.data.database.remote.firebase.base.FbBaseDao
import com.demo.kotlinexamples.data.database.remote.firebase.base.FbMap
import io.reactivex.Completable

class FbNumberListsDao(
    val firebaseDatabase: PhoneicFirebaseDatabase
) : FbBaseDao<NumberListsEntity>(), RemoteNumberListsDao {

    //this.getAll(Filter().add(FbNumberLists.lastUpdated, Filter.Type.GREATER_THAN_OR_EQUAL, ""))

    public override fun getAllRef(): DatabaseReference {
        return firebaseDatabase.userNumberListsNumbers
    }

    override fun getPagingOrderByQuery(): Query {
        return getAllRef().orderByKey()
    }

    fun insertOrUpdate(data : Map<String,Any>): Completable {
        return getAllRef().rxUpdateChildren(data)
    }

    override fun entityToMap(entity: NumberListsEntity): FbMap {
        return FbNumberListsConverter.entityToMap(entity)
    }

    override fun snapshotToEntity(dataSnapshot: DataSnapshot): NumberListsEntity {
        return FbNumberListsConverter.snapshotToEntity(dataSnapshot)
    }

    override fun childEventToEntityEvent(childEvent: ChildEvent): EntityEvent {
        return FbNumberListsConverter.childEventToEntityEvent(childEvent)
    }

    override fun filterKeyToFirebaseKey(key: String): String {
        return when (key) {
            NumberListsEntity.PRIMARY_KEY -> FbNumberLists.contact_number
            else -> super.filterKeyToFirebaseKey(key)
        }
    }
}
