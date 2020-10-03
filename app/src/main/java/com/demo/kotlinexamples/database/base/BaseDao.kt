package com.demo.kotlinexamples.data.database.base

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface BaseDao<TEntity : BaseEntity> {

    // Get all data once

    fun getAll(filter: Filter? = null): Single<List<TEntity>>
    fun getAll(entities: List<TEntity>): Single<List<TEntity>> = getAllByUid(entities.map { it.uidDesc.value })
    fun getAll(size: Int): Single<List<TEntity>>
    fun getAllAfter(key: String, size: Int): Single<List<TEntity>>
    fun getAllBefore(key: String, size: Int): Single<List<TEntity>>
    fun getAllByUid(uidList: List<String>): Single<List<TEntity>>

    // Get all data and its changes

    fun getAllObservable(filter: Filter? = null): Flowable<List<TEntity>>
    fun getAllObservable(entities: List<TEntity>): Flowable<List<TEntity>> = getAllByUidObservable(entities.map { it.uidDesc.value })
    fun getAllObservable(size: Int): Flowable<List<TEntity>>
    fun getAllAfterObservable(key: String, size: Int): Flowable<List<TEntity>>
    fun getAllBeforeObservable(key: String, size: Int): Flowable<List<TEntity>>
    fun getAllByUidObservable(uidList: List<String>): Flowable<List<TEntity>>

    // Get data once

    fun get(filter: Filter? = null): Single<TEntity>
    fun get(entity: TEntity): Single<TEntity> = get(entity.uidDesc.value)
    fun get(uid: String): Single<TEntity>

    // Get data and its changes

    fun getObservable(filter: Filter? = null): Flowable<TEntity>
    fun getObservable(entity: TEntity): Flowable<TEntity> = getObservable(entity.uidDesc.value)
    fun getObservable(uid: String): Flowable<TEntity>

    // Insert new or do nothing if exists, return UID

    fun insert(entity: TEntity): Single<String>
    fun insert(entities: List<TEntity>): Single<List<String>>

    // Update if exists, return number of updated entities

    fun update(entity: TEntity): Single<Int>
    fun update(entities: List<TEntity>): Single<Int>

    // Insert new or update if exist, return UID

    fun insertOrUpdate(entity: TEntity): Single<String>
    fun insertOrUpdate(entities: List<TEntity>): Single<List<String>>

    // Delete if exist, return number of deleted entities

    fun delete(entity: TEntity): Single<Int>
    fun delete(entities: List<TEntity>): Single<Int>

    fun deleteAll(): Completable
}
