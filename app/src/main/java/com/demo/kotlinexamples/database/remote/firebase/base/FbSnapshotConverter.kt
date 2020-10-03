package com.demo.kotlinexamples.data.database.remote.firebase.base

import com.androidhuman.rxfirebase2.database.*
import com.demo.kotlinexamples.util.unsupportedOperation
import com.google.firebase.database.DataSnapshot
import com.demo.kotlinexamples.data.database.base.*

abstract class FbSnapshotConverter<TEntity : BaseEntity> {
    fun childEventToEntityEvent(childEvent: ChildEvent): EntityEvent =
        when(childEvent) {
            is ChildAddEvent -> EntityAddEvent(EventData(snapshotToEntity(childEvent.dataSnapshot())))
            is ChildChangeEvent -> EntityChangeEvent(EventData(snapshotToEntity(childEvent.dataSnapshot())))
            is ChildMoveEvent -> EntityMoveEvent(EventData(snapshotToEntity(childEvent.dataSnapshot())))
            is ChildRemoveEvent -> EntityRemoveEvent(EventData(snapshotToEntity(childEvent.dataSnapshot())))
            else -> throw IllegalArgumentException("ChildEvent |childEvent| unsupported event type! childEvent=${childEvent.javaClass.name}")
        }

    abstract fun snapshotToEntity(dataSnapshot: DataSnapshot): TEntity

    open fun entityToMap(entity: TEntity): FbMap =
        unsupportedOperation("You are trying to rewrite the read-only DAO")
}
