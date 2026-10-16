package com.example.jeeptours.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jeeptours.domain.TripOrder

@Entity(tableName = "trip_orders")
data class TripOrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val locationName: String,
    val vehicleClassTitle: String,
    val riskPercentage: Int,
    val totalCost: Int,
    val timestamp: Long
) {
    fun toDomainModel() = TripOrder(id, locationName, vehicleClassTitle, riskPercentage, totalCost, timestamp)

    companion object {
        fun fromDomainModel(order: TripOrder) = TripOrderEntity(
            id = order.id,
            locationName = order.locationName,
            vehicleClassTitle = order.vehicleClassTitle,
            riskPercentage = order.riskPercentage,
            totalCost = order.totalCost,
            timestamp = order.timestamp
        )
    }
}