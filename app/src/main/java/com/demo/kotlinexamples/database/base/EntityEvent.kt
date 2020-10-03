package com.demo.kotlinexamples.data.database.base

sealed class EntityEvent

data class EntityAddEvent(val data: EventData) : EntityEvent()
data class EntityChangeEvent(val data: EventData) : EntityEvent()
data class EntityMoveEvent(val data: EventData) : EntityEvent()
data class EntityRemoveEvent(val data: EventData) : EntityEvent()

data class EventData(private val entity: BaseEntity) {
    fun <T : BaseEntity> getValue(clazz: Class<T>): T = clazz.cast(entity)!!
}
