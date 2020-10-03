package com.demo.kotlinexamples.data.database.remote.firebase.base

typealias FbMap = Map<String, Any?>
typealias FbMapOf<V> = Map<String, V>

typealias FbMutableMap = MutableMap<String, Any?>
typealias FbMutableMapOf<V> = MutableMap<String, V>

typealias FbHashMap = HashMap<String, Any?>
typealias FbHashMapOf<V> = HashMap<String, V>

@Suppress("UNCHECKED_CAST")
fun Any?.toFbMap(): FbMap? = this as? FbMap

@Suppress("UNCHECKED_CAST")
fun Any?.toFbMutableMap(): FbMutableMap? = this as? FbMutableMap

@Suppress("UNCHECKED_CAST")
fun Any?.toFbHashMap(): FbHashMap? = this as? FbHashMap
