package com.demo.kotlinexamples.database.local.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.demo.kotlinexamples.data.database.common.entity.BookListsEntity
import com.demo.kotlinexamples.data.database.local.dao.LocalDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single


@Dao
abstract class BookListsDao : LocalDao<BookListsEntity>(
    tableName = BookListsEntity.TABLE_NAME,
    primaryKey = BookListsEntity.PRIMARY_KEY
) {
    @RawQuery(observedEntities = [BookListsEntity::class])
    abstract override fun getAllObservable(query: SupportSQLiteQuery): Flowable<List<BookListsEntity>>

    @RawQuery(observedEntities = [BookListsEntity::class])
    abstract override fun getObservable(query: SupportSQLiteQuery): Flowable<BookListsEntity>

    @Query("DELETE FROM number_lists")
    abstract override fun deleteAll(): Completable

    @Query("SELECT * FROM book_lists")
    abstract fun getData(): List<BookListsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertData(vararg users: BookListsEntity)
}