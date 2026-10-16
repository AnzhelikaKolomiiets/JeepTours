package com.example.jeeptours.domain.usecase.domain

import com.example.jeeptours.domain.model.LocationModel
import com.example.jeeptours.domain.model.VehicleClass

class CalculateTripCostUseCase {

    fun execute(location: LocationModel, vehicleClass: VehicleClass): Int {
        return location.durationHours * vehicleClass.baseRatePerHour
    }
}