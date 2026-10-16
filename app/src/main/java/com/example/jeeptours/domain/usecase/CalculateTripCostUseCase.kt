package com.example.jeeptours.domain

class CalculateTripCostUseCase {
    operator fun invoke(location: LocationModel, vehicle: VehicleClass): Int {
        return location.durationHours * vehicle.baseRatePerHour
    }
}