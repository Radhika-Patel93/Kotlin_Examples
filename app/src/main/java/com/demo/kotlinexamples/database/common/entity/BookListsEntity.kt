package com.demo.kotlinexamples.data.database.common.entity

import androidx.room.*
import com.demo.kotlinexamples.data.database.base.BaseEntity

@Entity(
    tableName = BookListsEntity.TABLE_NAME,
    indices = [Index(value = [BookListsEntity.PRIMARY_KEY], unique = true)]
)
data class BookListsEntity(
    @PrimaryKey
    @ColumnInfo(name = PRIMARY_KEY) val uid: String = "", // number itself (normalized)

    @ColumnInfo(name = BaseEntity.CREATED_AT_KEY) val createdAt: Long = 0L,
    @ColumnInfo(name = BaseEntity.UPDATED_AT_KEY) val updatedAt: Long = 0L,

    @ColumnInfo(name = "key") val key: String? = null,
    @ColumnInfo(name = "raw_data") val rawData: String? = null
) : BaseEntity {
    companion object {
        const val TABLE_NAME = "book_lists"
        const val PRIMARY_KEY = "uid"
    }

    @Ignore
    override val uidDesc = BaseEntity.UidDesc(PRIMARY_KEY, uid)

    data class Books(
        @Embedded(prefix = "source_") val source: Source? = null
    ) {
        data class Source(
            @Embedded(prefix = "demo_number_") val demoNumber: Number? = null,
            @Embedded(prefix = "pinhole_") val pinhole: Number? = null
        ) {
            data class Number(
                @ColumnInfo(name = "contact_number") val contactNumber: String? = null,
                @ColumnInfo(name = "create_only_if_not_exists") val createOnlyIfNotExists: Boolean? = null,
                @ColumnInfo(name = "edit_if_exists") val editIfExists: Boolean? = null,
                @ColumnInfo(name = "new_company_name") val newCompanyName: String? = null,
                @ColumnInfo(name = "new_first_name") val newFirstName: String? = null,
                @ColumnInfo(name = "new_image") val newImage: String? = null,
                @ColumnInfo(name = "new_last_name") val newLastName: String? = null,
                @ColumnInfo(name = "new_middle_name") val newMiddleName: String? = null,
                @ColumnInfo(name = "new_number") val newNumber: String? = null,
                @ColumnInfo(name = "number_type") val numberType: String? = null
            )
        }
    }
}
