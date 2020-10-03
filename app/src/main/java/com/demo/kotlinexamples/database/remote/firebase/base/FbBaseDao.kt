package com.demo.kotlinexamples.data.database.remote.firebase.base

import com.androidhuman.rxfirebase2.database.*
import com.demo.kotlinexamples.database.remote.dao.RemoteDao
import com.demo.kotlinexamples.util.logw
import com.demo.kotlinexamples.util.unsupportedOperation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.demo.kotlinexamples.data.database.base.BaseEntity
import com.demo.kotlinexamples.data.database.base.EntityEvent
import com.demo.kotlinexamples.data.database.base.Filter
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

abstract class FbBaseDao<TEntity : BaseEntity> : RemoteDao<TEntity> {
    // Get all data once

    override fun getAll(filter: Filter?): Single<List<TEntity>> = getAllInternal(filter)
    override fun getAll(size: Int): Single<List<TEntity>> = getAllInternal(size)
    override fun getAllAfter(key: String, size: Int): Single<List<TEntity>> = getAllAfterInternal(key, size)
    override fun getAllBefore(key: String, size: Int): Single<List<TEntity>> = TODO("do we need this functionality here?")
    override fun getAllByUid(uidList: List<String>): Single<List<TEntity>> = getAllByUidInternal(uidList)

    // Get all data and its changes

    override fun getAllObservable(filter: Filter?): Flowable<List<TEntity>> = getAllObservableInternal(filter)
    override fun getAllObservable(size: Int): Flowable<List<TEntity>> = getAllObservableInternal(size)
    override fun getAllAfterObservable(key: String, size: Int): Flowable<List<TEntity>> = getAllAfterObservableInternal(key, size)
    override fun getAllBeforeObservable(key: String, size: Int): Flowable<List<TEntity>> = TODO("do we need this functionality here?")
    override fun getAllByUidObservable(uidList: List<String>): Flowable<List<TEntity>> = getAllByUidObservableInternal(uidList)

    // Get data once

    override fun get(filter: Filter?): Single<TEntity> = getInternal(filter)
    override fun get(uid: String): Single<TEntity> = getInternal(uid)

    // Get data and its changes

    override fun getObservable(filter: Filter?): Flowable<TEntity> = getObservableInternal(filter)
    override fun getObservable(uid: String): Flowable<TEntity> = getObservableInternal(uid)

    // Insert new or do nothing if exists, return UID or empty string

    override fun insert(entity: TEntity): Single<String> = insertOnConflictIgnoreInternal(entity)
    override fun insert(entities: List<TEntity>): Single<List<String>> = insertOnConflictIgnoreInternal(entities)

    // Update if exists, return number of updated entities

    override fun update(entity: TEntity): Single<Int> = updateIfExistInternal(entity)
    override fun update(entities: List<TEntity>): Single<Int> = updateIfExistInternal(entities)

    // Insert new or update if exist, return UID

    override fun insertOrUpdate(entity: TEntity): Single<String> = insertOrUpdateInternal(entity)
    override fun insertOrUpdate(entities: List<TEntity>): Single<List<String>> = insertOrUpdateInternal(entities)

    // Delete if exist, return number of deleted entities

    override fun delete(entity: TEntity): Single<Int> = deleteInternal(entity)
    override fun delete(entities: List<TEntity>): Single<Int> = deleteInternal(entities)

    override fun deleteAll(): Completable = getAllRef().rxRemoveValue()

    override fun listEvents(): Flowable<EntityEvent> = listEventsInternal()

    // PROTECTED section

    protected fun getItemRef(uid: String): DatabaseReference {
        return require(uid.isNotBlank()) { "String |uid| MUST BE non-blank!" }.let { getAllRef().child(uid) }
    }

    protected open fun snapshotToEntityList(dataSnapshot: DataSnapshot): List<TEntity> {
        return dataSnapshot.children.mapNotNull {
            try {
                snapshotToEntity(it)
            } catch (e: Throwable) {
                logw("snapshotToEntityList: error", e)
                null
            }
        }
    }

    protected open fun entityListToMap(entities: List<TEntity>): FbMap {
        return entities.map { it.uidDesc.value to entityToMap(it) }.toMap()
    }

    protected abstract fun getAllRef(): DatabaseReference

    protected abstract fun getPagingOrderByQuery(): Query

    protected abstract fun entityToMap(entity: TEntity): FbMap

    protected abstract fun snapshotToEntity(dataSnapshot: DataSnapshot): TEntity

    protected abstract fun childEventToEntityEvent(childEvent: ChildEvent): EntityEvent

    protected open fun overrideFilter(filter: Filter?): Filter? = filter

    protected open fun filterKeyToFirebaseKey(key: String): String =
        unsupportedOperation(
            "You are going to use one of the getXXX(filter) methods, " +
            "so please implement the filterKeyToFirebaseKey method " +
            "to add mapping for the filter key = $key"
        )

    protected open fun filterValueToFirebaseValue(value: Any?): Any? = value

    // PRIVATE section

    private fun getAllInternal(filter: Filter?): Single<List<TEntity>> {
        return filterToQuery(getAllRef(), filter).data().map(::snapshotToEntityList)
    }

    private fun getAllInternal(size: Int): Single<List<TEntity>> {
        return getPagingOrderByQuery().limitToFirst(size).data().map(::snapshotToEntityList)
    }

    private fun getAllAfterInternal(key: String, size: Int): Single<List<TEntity>> {
        return getPagingOrderByQuery()
            .startAt(key)
            .limitToFirst(size + 1)
            .data()
            .map { snapshotToEntityList(it).toMutableList().apply { if (isNotEmpty()) removeAt(0) } }
    }

    private fun getAllByUidInternal(uidList: List<String>): Single<List<TEntity>> {
        return Single.merge(uidList.map(::get)).toList()
    }

    private fun getAllObservableInternal(filter: Filter?): Flowable<List<TEntity>> {
        return filterToQuery(getAllRef(), filter).dataChanges(BackpressureStrategy.LATEST).map(::snapshotToEntityList)
    }

    private fun getAllObservableInternal(size: Int): Flowable<List<TEntity>> {
        return getPagingOrderByQuery().limitToFirst(size).dataChanges(BackpressureStrategy.LATEST).map(::snapshotToEntityList)
    }

    private fun getAllAfterObservableInternal(key: String, size: Int): Flowable<List<TEntity>> {
        return getPagingOrderByQuery()
            .startAt(key)
            .limitToFirst(size + 1)
            .dataChanges(BackpressureStrategy.LATEST)
            .map { snapshotToEntityList(it).toMutableList().apply { if (isNotEmpty()) removeAt(0) } }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getAllByUidObservableInternal(uidList: List<String>): Flowable<List<TEntity>> {
        return Flowable.combineLatest(uidList.map(::getObservable)) { array ->
            array.map { it as TEntity }.toList()
        }
    }

    private fun getInternal(filter: Filter?): Single<TEntity> {
        return filterToQuery(getAllRef(), filter, 1).data().map(::snapshotToEntity)
    }

    private fun getInternal(uid: String): Single<TEntity> {
        return getItemRef(uid).data().map(::snapshotToEntity)
    }

    private fun getObservableInternal(filter: Filter?): Flowable<TEntity> {
        return filterToQuery(getAllRef(), filter, 1).dataChanges(BackpressureStrategy.LATEST).map(::snapshotToEntity)
    }

    private fun getObservableInternal(uid: String): Flowable<TEntity> {
        return getItemRef(uid).dataChanges(BackpressureStrategy.LATEST).map(::snapshotToEntity)
    }

    private fun filterToQuery(ref: DatabaseReference, filter: Filter?, limit: Int = 0): Query {
        @Suppress("NAME_SHADOWING") val filter = overrideFilter(filter)

        if (filter != null && filter.size > 1) unsupportedOperation("Firebase do not support multiple orderBy clause :(")

        // if filter is null or size is 1

        var query: Query = ref.orderByPriority() // default behaviour

        filter?.forEach {
            val fbKey: String = filterKeyToFirebaseKey(it.key)
            val pair: Filter.TypeValuePair<*> = it.value
            val filterType: Filter.Type = pair.filterType
            val fbValue: Any? = filterValueToFirebaseValue(pair.value)

            query = when (fbKey) {
                FbSystemProperties.key -> ref.orderByKey()
                FbSystemProperties.priority -> ref.orderByPriority()
                FbSystemProperties.value -> ref.orderByValue()
                else -> ref.orderByChild(fbKey)
            }

            query = when {
                filterType == Filter.Type.EQUAL && fbValue is Double -> query.equalTo(fbValue)
                filterType == Filter.Type.EQUAL && fbValue is Boolean -> query.equalTo(fbValue)
                filterType == Filter.Type.EQUAL && fbValue is String -> query.equalTo(fbValue)
                filterType == Filter.Type.EQUAL && fbValue == null -> query.equalTo(fbValue)

                filterType == Filter.Type.GREATER_THAN_OR_EQUAL && fbValue is Double -> query.startAt(fbValue)
                filterType == Filter.Type.GREATER_THAN_OR_EQUAL && fbValue is Boolean -> query.startAt(fbValue)
                filterType == Filter.Type.GREATER_THAN_OR_EQUAL && fbValue is String -> query.startAt(fbValue)
                filterType == Filter.Type.GREATER_THAN_OR_EQUAL && fbValue == null -> query.startAt(fbValue)

                filterType == Filter.Type.LESS_THAN_OR_EQUAL && fbValue is Double -> query.endAt(fbValue)
                filterType == Filter.Type.LESS_THAN_OR_EQUAL && fbValue is Boolean -> query.endAt(fbValue)
                filterType == Filter.Type.LESS_THAN_OR_EQUAL && fbValue is String -> query.endAt(fbValue)
                filterType == Filter.Type.LESS_THAN_OR_EQUAL && fbValue == null -> query.endAt(fbValue)

                else -> unsupportedOperation("Filter [$filterType] or type of value [$fbValue] is unsupported by Firebase :(")
            }
        }

        if (limit > 0) {
            query = query.limitToFirst(limit)
        }

        return query
    }

    private fun insertOnConflictIgnoreInternal(entity: TEntity): Single<String> {
        // Note, we always using updateChildren no matter is we want INSERT or UPDATE
        // Since updateChildren will update/create only provided fields instead of setValue()
        // what will replace ALL exist data by new one

        val uid = entity.uidDesc.value
        return getItemRef(uid)
            .data()
            .flatMap {
                if (!it.exists()) {
                    // data not exist - perform insert
                    it.ref.rxUpdateChildren(entityToMap(entity)).toSingleDefault(uid)
                } else {
                    // data exist - ignore
                    Single.just(uid)
                }
            }
    }

    private fun insertOnConflictIgnoreInternal(entities: List<TEntity>): Single<List<String>> {
        return Single.merge(entities.map { insertOnConflictIgnoreInternal(it) }).toList()
    }

    private fun updateIfExistInternal(entity: TEntity): Single<Int> {
        // Note, we always using updateChildren no matter is we want INSERT or UPDATE
        // Since updateChildren will update/create only provided fields instead of setValue()
        // what will replace ALL exist data by new one

        return getItemRef(entity.uidDesc.value)
            .data()
            .flatMap {
                if (it.exists()) {
                    // data exist - perform update
                    it.ref.rxUpdateChildren(entityToMap(entity)).toSingleDefault(1)
                } else {
                    // data not exist - ignore
                    Single.just(0)
                }
            }
    }

    private fun updateIfExistInternal(entities: List<TEntity>): Single<Int> {
        return Single.merge(entities.map { updateIfExistInternal(it) })
            .toList()
            .map { list -> list.count { it != 0 } }
    }

    private fun insertOrUpdateInternal(entity: TEntity): Single<String> {
        // Note, we always using updateChildren no matter is we want INSERT or UPDATE
        // Since updateChildren will update/create only provided fields instead of setValue()
        // what will replace ALL exist data by new one

        val uid = entity.uidDesc.value
        return getItemRef(uid).rxUpdateChildren(entityToMap(entity)).toSingleDefault(uid)
    }

    private fun insertOrUpdateInternal(entities: List<TEntity>): Single<List<String>> {
        // Note, we always using updateChildren no matter is we want INSERT or UPDATE
        // Since updateChildren will update/create only provided fields instead of setValue()
        // what will replace ALL exist data by new one

        return getAllRef().rxUpdateChildren(entityListToMap(entities)).toSingleDefault(entities.map { it.uidDesc.value })
    }

    private fun deleteInternal(entity: TEntity): Single<Int> {
        return getItemRef(entity.uidDesc.value).rxRemoveValue().toSingleDefault(1)
    }

    private fun deleteInternal(entities: List<TEntity>): Single<Int> {
        val entitiesMap: FbMap = entities.map { it.uidDesc.value to null }.toMap()
        return getAllRef().rxUpdateChildren(entitiesMap).andThen(Single.just(entities.size))
    }

    private fun listEventsInternal(): Flowable<EntityEvent> {
        return getAllRef().childEvents(BackpressureStrategy.LATEST).map(::childEventToEntityEvent)
    }
}
