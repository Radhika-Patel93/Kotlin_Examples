package com.demo.kotlinexamples.data.database.local.dao

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.demo.kotlinexamples.util.unsupportedOperation
import com.demo.kotlinexamples.data.database.base.BaseDao
import com.demo.kotlinexamples.data.database.base.BaseEntity
import com.demo.kotlinexamples.data.database.base.Filter
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

abstract class LocalDao<TEntity : BaseEntity>(
    protected val tableName: String,
    protected val primaryKey: String,
    protected val pagingKey: String = primaryKey
) : BaseDao<TEntity> {

    // Get all data once

    final override fun getAll(filter: Filter?): Single<List<TEntity>> = getAllInternal(filter)
    final override fun getAll(size: Int): Single<List<TEntity>> = getAllInternal(size)
    final override fun getAllAfter(key: String, size: Int): Single<List<TEntity>> = getAllAfterInternal(key, size)
    override fun getAllBefore(key: String, size: Int): Single<List<TEntity>> = TODO("do we need this functionality here?")
    final override fun getAllByUid(uidList: List<String>): Single<List<TEntity>> = getAllByUidInternal(uidList)

    // Get all data and its changes

    final override fun getAllObservable(filter: Filter?): Flowable<List<TEntity>> = getAllObservableInternal(filter)
    final override fun getAllObservable(size: Int): Flowable<List<TEntity>> = getAllObservableInternal(size)
    final override fun getAllAfterObservable(key: String, size: Int): Flowable<List<TEntity>> = getAllAfterObservableInternal(key, size)
    override fun getAllBeforeObservable(key: String, size: Int): Flowable<List<TEntity>> = TODO("do we need this functionality here?")
    final override fun getAllByUidObservable(uidList: List<String>): Flowable<List<TEntity>> = getAllByUidObservableInternal(uidList)

    // Get data once

    final override fun get(filter: Filter?): Single<TEntity> = getInternal(filter)
    final override fun get(uid: String): Single<TEntity> = get(Filter().add(primaryKey, uid))

    // Get data and its changes

    final override fun getObservable(filter: Filter?): Flowable<TEntity> = getObservableInternal(filter)
    final override fun getObservable(uid: String): Flowable<TEntity> = getObservable(Filter().add(primaryKey, uid))

    // Insert new or do nothing if exists, return UID

    final override fun insert(entity: TEntity): Single<String> = insertOnConflictIgnoreInternal(entity).map { entity.uidDesc.value }
    final override fun insert(entities: List<TEntity>): Single<List<String>> = insertOnConflictIgnoreInternal(entities).map { entities.map { it.uidDesc.value } }

    // Update if exists, return number of updated entities

    final override fun update(entity: TEntity): Single<Int> = updateInternal(entity)
    final override fun update(entities: List<TEntity>): Single<Int> = updateInternal(entities)

    // Insert new or update if exist, return UID if success or empty string otherwise

    final override fun insertOrUpdate(entity: TEntity): Single<String> = Single.fromCallable { transactionInsertOrUpdate(entity) }
    final override fun insertOrUpdate(entities: List<TEntity>): Single<List<String>> = Single.fromCallable { transactionInsertOrUpdate(entities) }

    // Delete if exist, return number of deleted entities

    final override fun delete(entity: TEntity): Single<Int> = deleteInternal(entity)
    final override fun delete(entities: List<TEntity>): Single<Int> = deleteInternal(entities)

    override fun deleteAll(): Completable = unsupportedOperation()

    // PROTECTED section

    @RawQuery
    protected abstract fun getAll(query: SupportSQLiteQuery): Single<List<TEntity>>

    // if you wish to observe changes for query then override this method and define entity class in concrete DAO
    // @RawQuery(observedEntities = [YourEntity::class])
    protected abstract fun getAllObservable(query: SupportSQLiteQuery): Flowable<List<TEntity>>

    @RawQuery
    protected abstract fun get(query: SupportSQLiteQuery): Single<TEntity>

    // if you wish to observe changes for query then override this method and define entity class in concrete DAO
    // @RawQuery(observedEntities = [YourEntity::class])
    protected abstract fun getObservable(query: SupportSQLiteQuery): Flowable<TEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insertOnConflictIgnoreInternal(entity: TEntity): Single<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insertOnConflictIgnoreInternal(entities: List<TEntity>): Single<List<Long>>

    @Update
    protected abstract fun updateInternal(entity: TEntity): Single<Int>

    @Update
    protected abstract fun updateInternal(entities: List<TEntity>): Single<Int>

    @Delete
    protected abstract fun deleteInternal(entity: TEntity): Single<Int>

    @Delete
    protected abstract fun deleteInternal(entities: List<TEntity>): Single<Int>

    @Transaction
    protected open fun transactionInsertOrUpdate(entity: TEntity): String {
        // Transactions support only synchronous code, so blockingGet calls is required here
        val newId = insertOnConflictIgnoreInternal(entity).blockingGet()!!
        val updatedCount = if (newId == -1L && entity != get(entity).blockingGet()) update(entity).blockingGet()!! else 0
        return if (newId != -1L || updatedCount != 0) entity.uidDesc.value else ""
    }

    @Transaction
    protected open fun transactionInsertOrUpdate(entities: List<TEntity>): List<String> {
        // Transactions support only synchronous code, so blockingGet calls is required here
        val newIdList = insertOnConflictIgnoreInternal(entities).blockingGet()!!

        val entitiesToUpdateList = newIdList
            .mapIndexedNotNull { index, newId -> if (newId == -1L) entities[index] else null }
            // update only if really changed to reduce update events spam
            .mapNotNull { entity -> if (entity != get(entity).blockingGet()) entity else null }

        val insertedCount = entities.size - entitiesToUpdateList.size
        val updatedCount = if (entitiesToUpdateList.isNotEmpty()) update(entitiesToUpdateList).blockingGet()!! else 0
        val isUpdateSuccess = entitiesToUpdateList.size == updatedCount
        if (!isUpdateSuccess) {
            //throw RuntimeException("Some entities not updated, abort transaction")
        }

        // collect ids of inserted/updated items
        return newIdList
            // add ids of inserted items (where id != -1)
            .mapIndexed { index, newId -> if (newId != -1L) entities[index].uidDesc.value else null }
            .filterNotNull()
            .toMutableList()
            // add ids of updated items
            .apply { addAll(entitiesToUpdateList.map { it.uidDesc.value }) }
    }

    // PRIVATE section

    private fun getAllInternal(filter: Filter?): Single<List<TEntity>> {
        return getAll(filterToQuery(filter))
    }

    private fun getAllInternal(size: Int): Single<List<TEntity>> {
        return getAll(filterToQuery(null, size))
    }

    private fun getAllAfterInternal(key: String, size: Int): Single<List<TEntity>> {
        return getAll(filterToQuery(Filter().add(pagingKey, Filter.Type.GREATER_THAN, key), size))
    }

    private fun getAllObservableInternal(filter: Filter?): Flowable<List<TEntity>> {
        return getAllObservable(filterToQuery(filter))
    }

    private fun getAllObservableInternal(size: Int): Flowable<List<TEntity>> {
        return getAllObservable(filterToQuery(null, size))
    }

    private fun getAllAfterObservableInternal(key: String, size: Int): Flowable<List<TEntity>> {
        return getAllObservable(filterToQuery(Filter().add(pagingKey, Filter.Type.GREATER_THAN, key), size))
    }

    private fun getInternal(filter: Filter?): Single<TEntity> {
        // if filter is null then just return first one
        return get(filterToQuery(filter, if (filter == null) 1 else 0))
    }

    private fun getObservableInternal(filter: Filter?): Flowable<TEntity> {
        // if filter is null then just return first one
        return getObservable(filterToQuery(filter, if (filter == null) 1 else 0))
    }

    @Synchronized
    private fun filterToQuery(filter: Filter?, limit: Int = 0): SupportSQLiteQuery {
        var sql = "SELECT * FROM `$tableName`"

        if (!filter.isNullOrEmpty()) {
            sql += " WHERE"
        }

        val sqlArgs = arrayOfNulls<Any?>(filter?.size ?: 0)
        var index = 0

        filter?.forEach {
            val key: String = it.key
            val pair: Filter.TypeValuePair<*> = it.value
            val filterType: Filter.Type = pair.filterType
            val value: Any? = pair.value

            sql += if (index > 0) " AND" else ""

            sql += " `$key`"

            sql += when (filterType) {
                Filter.Type.EQUAL -> if (value == null) " IS ?" else " = ?"
                Filter.Type.NOT_EQUAL -> if (value == null) " IS NOT ?" else " != ?"
                Filter.Type.GREATER_THAN -> " > ?"
                Filter.Type.LESS_THAN -> " < ?"
                Filter.Type.GREATER_THAN_OR_EQUAL -> " >= ?"
                Filter.Type.LESS_THAN_OR_EQUAL -> " <= ?"
            }

            sqlArgs[index++] = value
        }

        var first = true
        filter?.forEach {
            val orderByKey: String = it.key

            sql += if (first) "" else ","
            sql += if (first) " ORDER BY `$orderByKey`" else " `$orderByKey`"

            first = false
        }

        if (!filter.isNullOrEmpty()) {
            val orderByAscending = true // todo: firebase do not support DESC
            sql += if (orderByAscending) " ASC" else " DESC"
        }

        if (limit > 0) {
            sql += " LIMIT $limit"
        }

        return SimpleSQLiteQuery(sql, sqlArgs)
    }

    private fun getAllByUidInternal(uidList: List<String>): Single<List<TEntity>> {
        return getAll(getAllByUidQuery(uidList))
    }

    private fun getAllByUidObservableInternal(uidList: List<String>): Flowable<List<TEntity>> {
        return getAllObservable(getAllByUidQuery(uidList))
    }

    private fun getAllByUidQuery(uidList: List<String>): SupportSQLiteQuery {
        val placeholders = uidList.joinToString(separator = ",", transform = { "?" })
        val sql = "SELECT * FROM `$tableName` WHERE `$primaryKey` IN ($placeholders)"
        return SimpleSQLiteQuery(sql, uidList.toTypedArray())
    }
}
