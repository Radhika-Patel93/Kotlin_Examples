package com.demo.kotlinexamples.data.database.remote.firebase.numberlists

import com.demo.kotlinexamples.util.logd
import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
import com.demo.kotlinexamples.data.database.common.entity.BookListsEntity
import com.demo.kotlinexamples.data.database.remote.firebase.base.FbMap
import com.demo.kotlinexamples.data.database.remote.firebase.base.FbSnapshotConverter
import com.demo.kotlinexamples.data.database.remote.firebase.base.toFbMap
import com.demo.kotlinexamples.domain.base.toSrvBoolean
import com.demo.kotlinexamples.domain.base.toSrvLong
import com.demo.kotlinexamples.domain.base.toSrvString
import com.demo.kotlinexamples.domain.extensions.asTimeMillis

object FbBookListsConverter : FbSnapshotConverter<BookListsEntity>() {

    override fun snapshotToEntity(dataSnapshot: DataSnapshot): BookListsEntity {
        val uid = requireNotNull(dataSnapshot.key) { "String |key| MUST NOT be null!" }
        val map = requireNotNull(dataSnapshot.value.toFbMap()) { "FbMap |map| MUST NOT be null!" }

        logd("snapshotToEntity: uid = $uid, map = $map")

        return BookListsEntity(
            uid = uid,
            createdAt = 0,
            updatedAt = map[FbBookLists.lastUpdated].toSrvLong().asTimeMillis(),
            key = map[FbBookLists.key].toString(),
            rawData = Gson().toJson(dataSnapshot.value as Map<String,Any>)
        )
    }

}
