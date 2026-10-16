package com.example.jeeptours.data

import com.example.jeeptours.domain.TripOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OrderRepository(private val dao: OrderDao) {
    fun getAllOrders(): Flow<List<TripOrder>> {
        return dao.getAllOrders().map { list -> list.map { it.toDomainModel() } }
    }

    suspend fun saveOrder(order: TripOrder) {
        dao.insertOrder(TripOrderEntity.fromDomainModel(order))
    }
}