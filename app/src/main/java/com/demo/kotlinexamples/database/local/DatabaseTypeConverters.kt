package com.demo.kotlinexamples.data.database.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.demo.kotlinexamples.data.database.base.SyncState
import com.demo.kotlinexamples.domain.base.toSrvString
import com.demo.kotlinexamples.domain.model.*
import com.demo.kotlinexamples.domain.model.contact.RecordingType
import java.util.*

object DatabaseTypeConverters {
    private val gson = Gson()

    @JvmStatic
    @TypeConverter
    fun stringToStringList(string: String): List<String> =
        if (string.isNotEmpty()) string.split(",") else listOf()

    @JvmStatic
    @TypeConverter
    fun stringListToString(list: List<String>): String =
        list.joinToString(",")

//    @JvmStatic
//    @TypeConverter
//    fun authTypeToString(authType: UserAccount.AuthType): String =
//        authType.toString()
//
//    @JvmStatic
//    @TypeConverter
//    fun stringToAuthType(string: String): UserAccount.AuthType =
//        UserAccount.AuthType.from(string)
//
//    @JvmStatic
//    @TypeConverter
//    fun callForwardingTypeToString(callForwardingType: UserAccount.CallForwardingType): String =
//        callForwardingType.toString()
//
//    @JvmStatic
//    @TypeConverter
//    fun stringToCallForwardingType(string: String): UserAccount.CallForwardingType =
//        UserAccount.CallForwardingType.from(string)

//    @JvmStatic
//    @TypeConverter
//    fun forwardNumberOptionListToString(list: List<ForwardNumberOption>): String =
//        gson.toJson(list, object : TypeToken<List<ForwardNumberOption>>() {}.type)

//    @JvmStatic
//    @TypeConverter
//    fun stringToForwardNumberOptionList(string: String): List<ForwardNumberOption> =
//        gson.fromJson(string, object : TypeToken<List<ForwardNumberOption>>() {}.type)

    @JvmStatic
    @TypeConverter
    fun dateToLong(date: Date): Long =
        date.time

    @JvmStatic
    @TypeConverter
    fun longToDate(long: Long): Date =
        Date(long)

    @JvmStatic
    @TypeConverter
    fun nullableAnyToNullableString(any: Any?): String? =
        any?.toSrvString()

    @JvmStatic
    @TypeConverter
    fun nullableStringToNullableAny(string: String?): Any? =
        string

    @JvmStatic
    @TypeConverter
    fun syncStateToString(syncState: SyncState): String =
        syncState.toString()

    @JvmStatic
    @TypeConverter
    fun stringToSyncState(string: String?): SyncState =
        SyncState.from(string)

//    @JvmStatic
//    @TypeConverter
//    fun completeStatusToString(completeStatus: CompleteStatus): String =
//        completeStatus.toString()
//
//    @JvmStatic
//    @TypeConverter
//    fun stringToCompleteStatus(string: String?): CompleteStatus =
//        CompleteStatus.from(string)
//
//    @JvmStatic
//    @TypeConverter
//    fun contentTypeToString(contentType: ContentType): String =
//        contentType.toString()
//
//    @JvmStatic
//    @TypeConverter
//    fun stringToContentType(string: String?): ContentType =
//        ContentType.from(string)
//
//    @JvmStatic
//    @TypeConverter
//    fun directionToString(direction: Direction): String =
//        direction.toString()
//
//    @JvmStatic
//    @TypeConverter
//    fun stringToDirection(string: String?): Direction =
//        Direction.from(string)
//
//    @JvmStatic
//    @TypeConverter
//    fun folderToString(folder: Folder): String =
//        folder.toString()
//
//    @JvmStatic
//    @TypeConverter
//    fun stringToFolder(string: String?): Folder =
//        Folder.from(string)
//
//    @JvmStatic
//    @TypeConverter
//    fun noteTypeToString(noteType: Note.Type): String =
//        noteType.toString()
//
//    @JvmStatic
//    @TypeConverter
//    fun stringToNoteType(string: String?): Note.Type =
//        Note.Type.from(string)

    @JvmStatic
    @TypeConverter
    fun recordingTypeToInt(recordingType: RecordingType): Int =
        recordingType.value

    @JvmStatic
    @TypeConverter
    fun intToRecordingType(int: Int?): RecordingType =
        RecordingType.from(int)
}
