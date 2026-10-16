package com.example.jeeptours.domain

enum class VehicleClass(
    val title: String,
    val maxAllowedDifficulty: Int,
    val baseRatePerHour: Int
) {
    LIGHT("Light SUV (Duster/Jimny)", maxAllowedDifficulty = 3, baseRatePerHour = 1000),
    HARD("Hard Offroad (Patrol/Wrangler)", maxAllowedDifficulty = 6, baseRatePerHour = 1500),
    EXTREME("Extreme Monster (Modified 4x4)", maxAllowedDifficulty = 10, baseRatePerHour = 2500)
}

data class LocationModel(
    val id: String,
    val name: String,
    val difficulty: Int, // 1..10
    val durationHours: Int,
    val heightMeters: Int
)

data class SafetyVerdict(
    val riskPercentage: Int,
    val isSafe: Boolean,
    val warningMessage: String,
    val recommendation: String
)

data class TripOrder(
    val id: Long = 0,
    val locationName: String,
    val vehicleClassTitle: String,
    val riskPercentage: Int,
    val totalCost: Int,
    val timestamp: Long = System.currentTimeMillis()
)