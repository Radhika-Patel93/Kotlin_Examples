package com.demo.kotlinexamples.data.database.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.demo.kotlinexamples.data.database.common.entity.NumberListsEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class NumberListsDao : LocalDao<NumberListsEntity>(
    tableName = NumberListsEntity.TABLE_NAME,
    primaryKey = NumberListsEntity.PRIMARY_KEY
) {
    @RawQuery(observedEntities = [NumberListsEntity::class])
    abstract override fun getAllObservable(query: SupportSQLiteQuery): Flowable<List<NumberListsEntity>>

    @RawQuery(observedEntities = [NumberListsEntity::class])
    abstract override fun getObservable(query: SupportSQLiteQuery): Flowable<NumberListsEntity>

    @Query("DELETE FROM number_lists")
    abstract override fun deleteAll(): Completable

    fun isExist(number: String): Single<Boolean> {
        return get(number).map { true }
            .onErrorReturnItem(false)
    }
}
