package com.demo.kotlinexamples.database.remote.dao

import com.demo.kotlinexamples.util.unsupportedOperation
import com.demo.kotlinexamples.data.database.base.BaseDao
import com.demo.kotlinexamples.data.database.base.BaseEntity
import com.demo.kotlinexamples.data.database.base.EntityEvent
import io.reactivex.Flowable

interface RemoteDao<TEntity : BaseEntity> : BaseDao<TEntity> {
    fun listEvents(): Flowable<EntityEvent> = unsupportedOperation()
}
