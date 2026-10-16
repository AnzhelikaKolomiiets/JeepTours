package com.example.jeeptours.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: TripOrderEntity)

    @Query("SELECT * FROM trip_orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<TripOrderEntity>>
}