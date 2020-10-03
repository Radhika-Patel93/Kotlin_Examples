package com.demo.kotlinexamples.data.database.remote.firebase.numberlists

import com.demo.kotlinexamples.util.logd
import com.google.firebase.database.DataSnapshot
import com.google.gson.Gson
import com.demo.kotlinexamples.data.database.common.entity.NumberListsEntity
import com.demo.kotlinexamples.data.database.remote.firebase.base.FbMap
import com.demo.kotlinexamples.data.database.remote.firebase.base.FbSnapshotConverter
import com.demo.kotlinexamples.data.database.remote.firebase.base.toFbMap
import com.demo.kotlinexamples.domain.base.toSrvBoolean
import com.demo.kotlinexamples.domain.base.toSrvLong
import com.demo.kotlinexamples.domain.base.toSrvString
import com.demo.kotlinexamples.domain.extensions.asTimeMillis

object FbNumberListsConverter : FbSnapshotConverter<NumberListsEntity>() {

    override fun snapshotToEntity(dataSnapshot: DataSnapshot): NumberListsEntity {
        val uid = requireNotNull(dataSnapshot.key) { "String |key| MUST NOT be null!" }
        val map = requireNotNull(dataSnapshot.value.toFbMap()) { "FbMap |map| MUST NOT be null!" }

        logd("snapshotToEntity: uid = $uid, map = $map")

        return NumberListsEntity(
            uid = uid,
            createdAt = 0,
            updatedAt = map[FbNumberLists.lastUpdated].toSrvLong().asTimeMillis(),
            contacts = mapToContacts(map[FbNumberLists.contacts].toFbMap()),
            whiteList = mapToWhiteList(map[FbNumberLists.whitelist].toFbMap()),
            blockList = mapToBlockList(map[FbNumberLists.blocklist].toFbMap()),
            rawData = Gson().toJson(dataSnapshot.value as Map<String,Any>)
        )
    }

    private fun mapToContacts(map: FbMap?): NumberListsEntity.Contacts {
        return NumberListsEntity.Contacts(
            source = mapToSource(map?.get(FbNumberLists.source).toFbMap())
        )
    }

    private fun mapToSource(map: FbMap?): NumberListsEntity.Contacts.Source {
        return NumberListsEntity.Contacts.Source(
            demoNumber = map?.get(FbNumberLists.demo_number).toFbMap()?.let { mapToNumber(it) },
            pinhole = map?.get(FbNumberLists.pinhole).toFbMap()?.let { mapToNumber(it) }
        )
    }

    private fun mapToNumber(map: FbMap): NumberListsEntity.Contacts.Source.Number {
        return NumberListsEntity.Contacts.Source.Number(
            contactNumber = map[FbNumberLists.contact_number].toSrvString(),
            createOnlyIfNotExists = map[FbNumberLists.create_only_if_not_exists].toSrvBoolean(),
            editIfExists = map[FbNumberLists.edit_if_exists].toSrvBoolean(),
            newCompanyName = map[FbNumberLists.new_company_name].toSrvString(),
            newFirstName = map[FbNumberLists.new_first_name].toSrvString(),
            newImage = map[FbNumberLists.new_image].toSrvString(),
            newLastName = map[FbNumberLists.new_last_name].toSrvString(),
            newMiddleName = map[FbNumberLists.new_middle_name].toSrvString(),
            newNumber = map[FbNumberLists.new_number].toSrvString(),
            numberType = map[FbNumberLists.number_type].toSrvString()
        )
    }

    private fun mapToWhiteList(map: FbMap?): String? {
        return map?.toString()
    }

    private fun mapToBlockList(map: FbMap?): String? {
        return map?.toString()
    }
}
